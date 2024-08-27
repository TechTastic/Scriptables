package io.github.techtastic.scriptables.api.lua.libraries

import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.VarArgFunction

class BlockEventLib: BaseEventLib() {
    override fun call(modname: LuaValue, env: LuaValue): LuaValue {
        super.call(modname, env)
        val event = env.get("event")

        event.set("stepOn", stepOn())

        return event
    }

    class stepOn: VarArgFunction()
}