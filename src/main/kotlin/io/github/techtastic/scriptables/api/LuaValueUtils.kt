package io.github.techtastic.scriptables.api

import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue

object LuaValueUtils {
    fun parse(o: Any?): LuaValue {
        return if (o is Int)
            LuaValue.valueOf(o)
        else if (o is Double)
            LuaValue.valueOf(o)
        else if (o is Float)
            LuaValue.valueOf(o.toDouble())
        else if (o is Boolean)
            LuaValue.valueOf(o)
        else if (o is String)
            LuaValue.valueOf(o)
        else if (o is ByteArray)
            LuaValue.valueOf(o)
        else if (o is Char)
            LuaValue.valueOf(o.toString())
        else if (o is Collection<*>)
            parseAsTable(o)
        else if (o is Array<*>)
            parseAsTable(o.toList())
        else if (o is IntArray)
            parseAsTable(o.toList())
        else if (o is DoubleArray)
            parseAsTable(o.toList())
        else if (o is FloatArray)
            parseAsTable(o.toList())
        else if (o is BooleanArray)
            parseAsTable(o.toList())
        else if (o is CharArray)
            parseAsTable(o.toList())
        else if (o is Map<*, *>)
            parseAsTable(o)
        else
            ScriptablesAPI.convertToAPI(o)
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
}