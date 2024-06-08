package io.github.techtastic.scriptables.api

import io.github.techtastic.scriptables.api.lib.ILuaLibrary
import io.github.techtastic.scriptables.api.scriptable.IScriptableProvider

object ScriptablesAPI {
    private val libraries = mutableListOf<ILuaLibrary>()
    private val providers = mutableListOf<IScriptableProvider>()

    fun registerCustomLibrary(library: ILuaLibrary) {
        this.libraries.find { it.getName() == library.getName() } ?: run {
            this.libraries.add(library)
            null
        } ?: throw Exception("Library named ${library.getName()} already exists!")
    }

    fun registerScriptableProvider(provider: IScriptableProvider) {
        this.providers.add(provider)
    }
}