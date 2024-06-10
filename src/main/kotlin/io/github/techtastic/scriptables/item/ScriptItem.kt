package io.github.techtastic.scriptables.item

import io.github.techtastic.scriptables.api.ScriptablesAPI
import io.github.techtastic.scriptables.api.lua.Script
import io.github.techtastic.scriptables.screen.ScriptEditorScreen
import net.minecraft.client.Minecraft
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
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
        // TODO: This should be a S2C packet tbh
        Minecraft.getInstance().setScreen(ScriptEditorScreen(scriptable.getScript()))

        return super.use(level, player, interactionHand)
    }
}