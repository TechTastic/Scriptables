package io.github.techtastic.scriptables.block.entity

import io.github.techtastic.scriptables.Scriptables.MOD_ID
import io.github.techtastic.scriptables.api.lua.LuaSandbox
import io.github.techtastic.scriptables.api.lua.libraries.BlockEventLib
import io.github.techtastic.scriptables.block.SBlockEntities
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs
import org.luaj.vm2.lib.TwoArgFunction
import org.luaj.vm2.lib.VarArgFunction

class ScriptableBE(blockPos: BlockPos, blockState: BlockState): BlockEntity(SBlockEntities.SCRIPTABLE_BLOCK.get(), blockPos, blockState) {
    var script = "event = require('event')\n" +
            "print('Hello World')\n" +
            "function event.stepOn(pos)\n" +
            "   print(tostring(pos.x))\n" +
            "end"
    var thread = LuaSandbox.runScript(this.script, libraries = this.getCustomLibraries())

    override fun saveAdditional(compoundTag: CompoundTag, provider: HolderLookup.Provider) {
        compoundTag.putString("$MOD_ID/script", this.script)

        super.saveAdditional(compoundTag, provider)
    }

    override fun loadAdditional(compoundTag: CompoundTag, provider: HolderLookup.Provider) {
        super.loadAdditional(compoundTag, provider)

        this.script = compoundTag.getString("$MOD_ID/script")
        this.thread = LuaSandbox.runScript(this.script, libraries = this.getCustomLibraries())
    }

    fun getCustomLibraries(): List<TwoArgFunction> {
        val libraries = mutableListOf<TwoArgFunction>()

        libraries.add(BlockEventLib())

        return libraries
    }

    fun handleEvent(event: String, args: Array<LuaValue> = arrayOf()): Varargs {
        try {
            return this.thread.globals.get("event").get(event).invoke(LuaValue.varargsOf(args))
        } catch (e: Exception) {
            println(e)
        }

        return LuaValue.varargsOf(arrayOf())
    }
}