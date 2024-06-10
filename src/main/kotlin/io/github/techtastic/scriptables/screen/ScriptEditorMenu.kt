package io.github.techtastic.scriptables.screen

import io.github.techtastic.scriptables.Scriptables.SCRIPT_EDITOR_MENU
import io.github.techtastic.scriptables.api.lua.LuaSandbox
import io.github.techtastic.scriptables.api.lua.Script
import io.github.techtastic.scriptables.api.scriptable.IScriptable
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack

class ScriptEditorMenu(syncId: Int, val scriptable: IScriptable) : AbstractContainerMenu(SCRIPT_EDITOR_MENU, syncId) {
    constructor(syncId: Int, inventory: Inventory): this(syncId, object: IScriptable {
        override fun getScript() = object: Script() {
            override fun reload() {
            }
        }
        override fun getOrCreateSandbox() = LuaSandbox()
    })

    fun getScript() = this.scriptable.getScript()
    fun getSandbox() = this.scriptable.getOrCreateSandbox()

    override fun quickMoveStack(player: Player, i: Int) = ItemStack.EMPTY
    override fun stillValid(player: Player) = true
}