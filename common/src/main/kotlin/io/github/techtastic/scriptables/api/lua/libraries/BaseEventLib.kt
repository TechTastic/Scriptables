package io.github.techtastic.scriptables.api.lua.libraries

import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction
import org.luaj.vm2.lib.VarArgFunction

open class BaseEventLib: TwoArgFunction() {
    override fun call(modname: LuaValue, env: LuaValue): LuaValue {
        val event = LuaTable()

        event.set("tick", tick())
        event.set("render", render())

        env.set("event", event)
        if (!env.get("package").isnil())
            env.get("package").get("loaded").set("event", event)

        return event
    }

    class tick: VarArgFunction()
    class render: VarArgFunction()
}