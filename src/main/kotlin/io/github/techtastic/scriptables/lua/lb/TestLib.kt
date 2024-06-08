package io.github.techtastic.scriptables.lua.lb

import io.github.techtastic.scriptables.Scriptables.LOGGER
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction
import org.luaj.vm2.lib.ZeroArgFunction

class TestLib: TwoArgFunction() {
    override fun call(modname: LuaValue, env: LuaValue): LuaValue {
        LOGGER.debug("Modname: $modname")

        val test = LuaTable(0, 30)
        test.set("testing", object: ZeroArgFunction() {
            override fun call(): LuaValue {
                LOGGER.debug("This was tested!")
                return LuaValue.NIL
            }
        })
        if (!env["package"].isnil())
            env["package"]["loaded"].set("test", test)
        return test
    }
}