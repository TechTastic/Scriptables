package io.github.techtastic.scriptables.screen

import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.item.ItemStack

class ScriptEditorMenu(menuType: MenuType<*>?, i: Int) : AbstractContainerMenu(menuType, i) {
    override fun quickMoveStack(player: Player, i: Int): ItemStack {
        TODO("Not yet implemented")
    }

    override fun stillValid(player: Player): Boolean {
        TODO("Not yet implemented")
    }
}