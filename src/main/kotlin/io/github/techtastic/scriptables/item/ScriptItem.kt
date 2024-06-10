package io.github.techtastic.scriptables.item

import io.github.techtastic.scriptables.api.ScriptablesAPI
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
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
        if (level !is ServerLevel)
            return InteractionResultHolder.sidedSuccess(player.getItemInHand(interactionHand) , true)

        val scriptable = ScriptablesAPI.findScriptable(level, player, interactionHand)
            ?: return InteractionResultHolder.pass(player.getItemInHand(interactionHand))

        scriptable.openScreenOnClient(player as ServerPlayer)

        return InteractionResultHolder.success(player.getItemInHand(interactionHand))
    }
}