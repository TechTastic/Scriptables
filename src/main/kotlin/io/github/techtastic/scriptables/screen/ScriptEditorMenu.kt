package io.github.techtastic.scriptables.screen

import io.github.techtastic.scriptables.Scriptables.SCRIPTABLE_BLOCK_MENU_TYPE
import io.github.techtastic.scriptables.block.ScriptableBlockEntity
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack

class ScriptEditorMenu(syncId: Int, val inventory: Inventory, val data: ScriptableBlockEntity.ScriptableBlockData) : AbstractContainerMenu(SCRIPTABLE_BLOCK_MENU_TYPE, syncId) {
    val be = inventory.player.level().getBlockEntity(data.pos) as ScriptableBlockEntity

    fun getScript() = data.script
    fun getFormattedScript(): String {
        var script = ""
        this.getScript().forEach {
            script += it + "\n"
        }
        return script
    }
    fun getLog() = be.logs
    fun runOnSandbox(snippet: String) = be.runOnSnadbox(snippet)

    override fun quickMoveStack(player: Player, i: Int) = ItemStack.EMPTY
    override fun stillValid(player: Player) = true
}