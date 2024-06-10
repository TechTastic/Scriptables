package io.github.techtastic.scriptables.item

import io.github.techtastic.scriptables.api.ScriptablesAPI
import io.github.techtastic.scriptables.screen.ScriptEditorMenu
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class ScriptItem(properties: Properties) : Item(properties) {
    override fun use(
        level: Level,
        player: Player,
        interactionHand: InteractionHand
    ): InteractionResultHolder<ItemStack> {
        val scriptable = ScriptablesAPI.findScriptable(level, player, interactionHand) ?: return super.use(level, player, interactionHand)
        player.openMenu(object: MenuProvider {
            override fun createMenu(i: Int, inventory: Inventory, player: Player) = ScriptEditorMenu(i, scriptable)
            override fun getDisplayName() = Component.translatable("gui.scriptables.script_editor")
        })

        return super.use(level, player, interactionHand)
    }
}