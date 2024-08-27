package io.github.techtastic.scriptables.api.lua.wrappers

import net.minecraft.core.BlockPos
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

class LuaBlockPos(blockPos: BlockPos): LuaTable() {
    init {
        this.set("x", LuaValue.valueOf(blockPos.x))
        this.set("y", LuaValue.valueOf(blockPos.y))
        this.set("z", LuaValue.valueOf(blockPos.z))
    }
}