package io.github.techtastic.scriptables.api.lua

import io.github.techtastic.scriptables.api.LuaValueUtils
import io.github.techtastic.scriptables.api.ScriptablesAPI
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.ThreeArgFunction
import org.luaj.vm2.lib.TwoArgFunction
import org.luaj.vm2.lib.VarArgFunction
import org.luaj.vm2.lib.ZeroArgFunction
import java.lang.invoke.MethodHandles
import java.lang.invoke.MethodType

interface ILuaAPI {
    fun getName(): String
    fun toLibrary(): TwoArgFunction {
        // The table that will contain the library methods
        val lib = LuaTable()

        // Get all methods in the class
        val methods = this::class.java.methods.clone().toMutableList()

        // Remove all non-Annotated methods and methods with parameters and returns which cannot be parsed as LuaValue
        methods.removeIf { method ->
            !method.isAnnotationPresent(LuaAccessible::class.java) ||
                    !ScriptablesAPI.hasAPI(method.returnType) ||
                    run {
                        var invalidParameter = false

                        method.parameters.forEach { parameter ->
                            invalidParameter = !ScriptablesAPI.hasAPI(parameter::class.java)
                        }

                        invalidParameter
                    }
        }

        // Add method call to library table using method's name
        methods.forEach { method ->
            lib.set(method.name,
                when (method.parameterCount) {
                    0 -> object: ZeroArgFunction() {
                        override fun call() =
                            LuaValueUtils.parse(MethodHandles.zero(this@ILuaAPI::class.java).invokeWithArguments(method.parameters.toList()))
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
            )
        }

        // This is the actual library setup, adding the library under the name in getName()
        return object: TwoArgFunction() {
            override fun call(modname: LuaValue, env: LuaValue): LuaValue {
                if (!env["package"].isnil())
                    env["package"]["loaded"].set(this@ILuaAPI.getName(), lib)
                return lib
            }
        }
    }
}