package io.github.techtastic.scriptables.api

import io.github.techtastic.scriptables.Scriptables.LOGGER
import io.github.techtastic.scriptables.api.lua.ILuaAPI
import io.github.techtastic.scriptables.api.lua.LuaAccessible
import io.github.techtastic.scriptables.api.scriptable.IScriptableProvider
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs
import org.luaj.vm2.lib.*
import java.lang.invoke.MethodHandles
import java.lang.invoke.MethodType
import java.lang.reflect.Method
import kotlin.reflect.KCallable
import kotlin.reflect.KFunction
import kotlin.reflect.full.findAnnotations
import kotlin.reflect.jvm.jvmErasure

object ScriptablesAPI {


    private val apis = mutableMapOf<Class<*>, Class<in ILuaAPI>>()
    private val providers = mutableListOf<IScriptableProvider>()

    fun registerAPI(clazz: Class<*>, api: ILuaAPI) {
        if (this.apis.containsKey(clazz))
            return

        this.apis[clazz] = api.javaClass
    }

    fun registerScriptableProvider(provider: IScriptableProvider) {
        this.providers.add(provider)
    }

    fun convertToAPI(o: Any): TwoArgFunction? {
        val api = this.apis[o::class.java]?.getConstructor(o::class.java)?.newInstance(o)
        return if (api is ILuaAPI)
            parseAPI(api)
        else
            null
    }

    fun hasAPI(o: Class<*>) =
        this.apis.containsKey(o)

    fun parseAPI(api: ILuaAPI): TwoArgFunction {
        LOGGER.info("Parsing '${api.getName()}' API...")
        // The table that will contain the library methods
        val lib = LuaTable()

        // Get all methods in the class
        //val methods = api::class.java.methods.clone().toMutableList()
        val kMethods = api::class.members.toMutableList()

        // Remove all non-Annotated methods and methods with parameters and returns which cannot be parsed as LuaValue
        //methods.retainAll(this::isValidLuaFunction)
        kMethods.retainAll(this::isValidLuaFunction)

        // Add method call to library table using method's name
        /*methods.forEach { method ->
            LOGGER.info("Parsing Method '${method.name}' with ${method.parameterCount} parameters...")
            lib.set(method.name, parseFunction(api, method))
        }*/
        kMethods.forEach { function ->
            LOGGER.info("Parsing Method `${function.name}' with ${function.parameters.size} parameters...")
            lib.set(function.name, parseFunction(api, function))
        }

        // This is the actual library setup, adding the library under the name in getName()
        return object: TwoArgFunction() {
            override fun call(modname: LuaValue, env: LuaValue): LuaValue {
                if (!env["package"].isnil())
                    env["package"]["loaded"].set(api.getName(), lib)
                return lib
            }
        }
    }

    /*fun parseFunction(api: ILuaAPI, method: Method): LibFunction {
        return when (method.parameterCount) {
            0 -> object: ZeroArgFunction() {
                override fun call() =
                    LuaValueUtils.parse(MethodHandles.zero(api::class.java).invokeWithArguments(method.parameters.toList()))
            }
            1 -> object: OneArgFunction() {
                override fun call(arg: LuaValue) =
                    LuaValueUtils.parse(MethodHandles.exactInvoker(MethodType.genericMethodType(1)).invokeWithArguments(method.parameters.toList()))
            }
            2 -> object: TwoArgFunction() {
                override fun call(arg1: LuaValue, arg2: LuaValue) =
                    LuaValueUtils.parse(MethodHandles.exactInvoker(MethodType.genericMethodType(2)).invokeWithArguments(method.parameters.toList()))
            }
            3 -> object: ThreeArgFunction() {
                override fun call(arg1: LuaValue, arg2: LuaValue, arg3: LuaValue) =
                    LuaValueUtils.parse(MethodHandles.exactInvoker(MethodType.genericMethodType(3)).invokeWithArguments(method.parameters.toList()))
            }
            else -> object: VarArgFunction() {
                override fun invoke(args: Varargs) =
                    LuaValueUtils.parse(MethodHandles.exactInvoker(MethodType.genericMethodType(args.narg())).invoke(args))
            }
        }
    }*/

    fun parseFunction(api: ILuaAPI, function: KCallable<*>): LibFunction {
        return when (function.parameters.size) {
            0 -> object: ZeroArgFunction() {
                override fun call() =
                    LuaValueUtils.parse(MethodHandles.zero(api::class.java).invokeWithArguments(function.parameters))
            }
            1 -> object: OneArgFunction() {
                override fun call(arg: LuaValue) =
                    LuaValueUtils.parse(MethodHandles.exactInvoker(MethodType.genericMethodType(1)).invokeWithArguments(function.parameters))
            }
            2 -> object: TwoArgFunction() {
                override fun call(arg1: LuaValue, arg2: LuaValue) =
                    LuaValueUtils.parse(MethodHandles.exactInvoker(MethodType.genericMethodType(2)).invokeWithArguments(function.parameters))
            }
            3 -> object: ThreeArgFunction() {
                override fun call(arg1: LuaValue, arg2: LuaValue, arg3: LuaValue) =
                    LuaValueUtils.parse(MethodHandles.exactInvoker(MethodType.genericMethodType(3)).invokeWithArguments(function.parameters))
            }
            else -> object: VarArgFunction() {
                override fun invoke(args: Varargs) =
                    LuaValueUtils.parse(MethodHandles.exactInvoker(MethodType.genericMethodType(args.narg())).invoke(args))
            }
        }
    }

    /*fun isValidLuaFunction(method: Method): Boolean {
        val isAnnotated = method.isAnnotationPresent(LuaAccessible::class.java)
        if (!isAnnotated)
            return false

        val isValidReturn = LuaValueUtils.isValidClass(method.returnType)
        if (!isValidReturn) {
            LOGGER.error("Return Value (${method.returnType.name}) of Method ${method.name} is not a valid returnable. Skipping annotated method...")
            return false
        }

        var hasInvalidParameter = false
        method.parameters.forEach { parameter ->
            hasInvalidParameter = !LuaValueUtils.isValidClass(parameter.type)
        }
        if (hasInvalidParameter)
            LOGGER.error("One of the parameters of Method ${method.name} is not a valid parameter. Skipping annotated method...")

        return !hasInvalidParameter
    }*/

    fun isValidLuaFunction(function: KCallable<*>): Boolean {
        if (function.findAnnotations(LuaAccessible::class).isEmpty())
            return false

        if (!LuaValueUtils.isValidClass(function.returnType.jvmErasure)) {
            LOGGER.error("Return Value (${function.returnType.jvmErasure}) of Method '${function.name}' is not a valid returnable. Skipping annotated method...")
            return false
        }

        val count = function.parameters.count {
            LOGGER.info(it.name)
            !LuaValueUtils.isValidClass(it.type.jvmErasure)
        }
        if (count > 0)
            LOGGER.error("$count perameters of Method '${function.name}' are not valid parameters. Skipping annotated method...")

        return count == 0
    }
}