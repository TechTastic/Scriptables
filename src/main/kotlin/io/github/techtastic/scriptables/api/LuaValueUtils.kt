package io.github.techtastic.scriptables.api

import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import kotlin.reflect.KClass

object LuaValueUtils {
    fun parse(o: Any?): LuaValue {
        if (o == null || !isValidClass(o::class.java))
            return LuaValue.NIL

        return when (o) {
            is LuaValue -> o
            is Int -> LuaValue.valueOf(o)
            is Double -> LuaValue.valueOf(o)
            is Float -> LuaValue.valueOf(o.toDouble())
            is Boolean -> LuaValue.valueOf(o)
            is String -> LuaValue.valueOf(o)
            is ByteArray -> LuaValue.valueOf(o)
            is Char -> LuaValue.valueOf(o.toString())
            is Collection<*> -> parseAsTable(o)
            is Array<*> -> parseAsTable(o.toList())
            is IntArray -> parseAsTable(o.toList())
            is DoubleArray -> parseAsTable(o.toList())
            is FloatArray -> parseAsTable(o.toList())
            is BooleanArray -> parseAsTable(o.toList())
            is CharArray -> parseAsTable(o.toList())
            is Map<*, *> -> parseAsTable(o)
            else -> ScriptablesAPI.convertToAPI(o) ?: LuaValue.NIL
        }
    }

    fun parseAsTable(collection: Collection<*>): LuaTable {
        val table = LuaTable()

        collection.forEach {
            val value = this.parse(it)
            if (value != LuaValue.NIL)
                table.add(value)
        }

        return table
    }

    fun parseAsTable(map: Map<*, *>): LuaTable {
        val table = LuaTable()

        map.forEach { (key, value) ->
            val luaKey = this.parse(key)
            if (luaKey == LuaValue.NIL)
                return@forEach
            val luaValue = this.parse(value)
            if (luaValue == LuaValue.NIL)
                return@forEach
            table.set(luaKey, luaValue)
        }

        return table
    }

    fun isValidClass(clazz: KClass<*>) =
        clazz == Int::class ||
                clazz == Double::class ||
                clazz == Float::class ||
                clazz == Boolean::class ||
                clazz == ByteArray::class ||
                clazz == Char::class ||
                clazz == Collection::class ||
                clazz == Array::class ||
                clazz == IntArray::class ||
                clazz == DoubleArray::class ||
                clazz == FloatArray::class ||
                clazz == BooleanArray::class ||
                clazz == CharArray::class ||
                clazz == Map::class ||
                clazz == Unit::class ||
                clazz == LuaValue::class ||
                ScriptablesAPI.hasAPI(clazz.java)

    fun isValidClass(clazz: Class<*>) =
        this.isValidClass(clazz.kotlin)
}