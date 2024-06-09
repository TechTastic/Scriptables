package io.github.techtastic.scriptables.lua.lb

import io.github.techtastic.scriptables.Scriptables.LOGGER
import io.github.techtastic.scriptables.api.lua.ILuaAPI
import io.github.techtastic.scriptables.api.lua.LuaAccessible
import java.util.UUID
import kotlin.random.Random

class TestLib(): ILuaAPI {
    /*override fun call(modname: LuaValue, env: LuaValue): LuaValue {
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
    }*/
    override fun getName() = "test"

    @LuaAccessible
    fun testing() {
        LOGGER.debug("This was tested!")
    }

    @LuaAccessible
    fun getRandomInt() = Random.nextInt()

    @LuaAccessible
    fun getRandomDouble() = Random.nextDouble()

    @LuaAccessible
    fun getRandomFloat() = Random.nextFloat()

    @LuaAccessible
    fun getUUIDString() = UUID.randomUUID().toString()

    val bool: Boolean = true
        @LuaAccessible get

    @LuaAccessible
    var arr = arrayOf("Hello", "World!")
}