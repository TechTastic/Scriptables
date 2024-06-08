package io.github.techtastic.scriptables.api

import io.github.techtastic.scriptables.api.lua.ILuaAPI
import io.github.techtastic.scriptables.api.scriptable.IScriptableProvider
import org.luaj.vm2.LuaValue

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

    fun convertToAPI(o: Any?): LuaValue = o?.let {
        (this.apis[o::class.java]?.getConstructor(o::class.java)?.newInstance(o) as ILuaAPI).toLibrary()
    } ?: LuaValue.NIL

    fun hasAPI(o: Class<*>) =
        this.apis.containsKey(o)
}