package io.github.techtastic.scriptables.util

import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player

class NamelessInventory(player: Player) : Inventory(player) {
    override fun getDisplayName() = Component.empty()
}