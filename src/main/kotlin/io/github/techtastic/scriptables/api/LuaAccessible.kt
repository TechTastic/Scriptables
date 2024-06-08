package io.github.techtastic.scriptables.api

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY)
annotation class LuaAccessible(val luaName: String)