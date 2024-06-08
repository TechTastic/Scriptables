package io.github.techtastic.scriptables.api.lib

import io.github.techtastic.scriptables.api.LuaAccessible
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.ZeroArgFunction
import java.lang.invoke.MethodHandleProxies
import java.lang.invoke.MethodHandles
import java.lang.invoke.MethodType

interface ILuaLibrary {
    fun getName(): String

    fun toLibrary() {
        val lib = LuaTable(0, 30)
        this.javaClass.declaredMethods.forEach {
            if (it.isAnnotationPresent(LuaAccessible::class.java))
                return@forEach
            //TODO: Check Parameters
            it.parameters.forEach {
            }
            //TODO: Check Return Value
            lib.set(it.getAnnotation(LuaAccessible::class.java).luaName, when(it.parameterCount) {
                0 -> object: ZeroArgFunction() {
                    override fun call(): LuaValue {
                        //TODO: Invoke Method and cast to LuaValue
                        return MethodHandleProxies.wrapperInstanceTarget(it).invokeWithArguments(it.parameters.toList())
                    }

                }
            })
        }
    }
}