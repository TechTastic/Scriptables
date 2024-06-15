package io.github.techtastic.scriptables.screen

import io.github.techtastic.scriptables.Scriptables.LOGGER
import io.github.techtastic.scriptables.Scriptables.SCRIPTABLE_BLOCK_MENU_TYPE
import io.github.techtastic.scriptables.block.ScriptableBlockEntity
import io.github.techtastic.scriptables.networking.packet.ScriptableBlockRunnablePayload
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack

class ScriptEditorMenu(val syncId: Int, val inventory: Inventory, val data: ScriptableBlockEntity.ScriptableBlockData) : AbstractContainerMenu(SCRIPTABLE_BLOCK_MENU_TYPE, syncId) {
    val be = inventory.player.level().getBlockEntity(data.pos) as ScriptableBlockEntity
    val script = data.script

    init {
    }

    fun getLog() = be.logs
    fun runOnSandbox(snippet: String) {
        if (be.level is ServerLevel) {
            be.runOnSandbox(snippet)
            return
        }

        ClientPlayNetworking.send(ScriptableBlockRunnablePayload(this.data.pos, snippet))
    }

    override fun clickMenuButton(player: Player, i: Int): Boolean {
        when (i) {
            0 -> LOGGER.info("Upload Attempted!")
            1 -> LOGGER.info("Save Attempted!")
            2 -> LOGGER.info("File Open Attempted!")
            3 -> LOGGER.info("Console Run Attempted!")
            else -> super.clickMenuButton(player, i)
        }
        return super.clickMenuButton(player, i)
    }

    override fun quickMoveStack(player: Player, i: Int) = ItemStack.EMPTY
    override fun stillValid(player: Player) = true
}