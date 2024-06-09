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
    private val providers = mutableListOf<IScriptableProvider>()

    fun registerScriptableProvider(provider: IScriptableProvider) {
        this.providers.add(provider)

    }
}