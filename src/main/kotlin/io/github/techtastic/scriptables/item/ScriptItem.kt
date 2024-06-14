package io.github.techtastic.scriptables.item

import io.github.techtastic.scriptables.block.ScriptableBlockEntity
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.Level
import net.minecraft.world.phys.shapes.CollisionContext

class ScriptItem(properties: Properties) : Item(properties) {
    override fun use(
        level: Level,
        player: Player,
        interactionHand: InteractionHand
    ): InteractionResultHolder<ItemStack> {
        if (level !is ServerLevel)
            return InteractionResultHolder.sidedSuccess(player.getItemInHand(interactionHand) , true)

        val hitResult = level.clip(
            ClipContext(
                player.eyePosition,
                player.eyePosition.add(
                    player.lookAngle.multiply(
                        player.blockInteractionRange(),
                        player.blockInteractionRange(),
                        player.blockInteractionRange()
                    )
                ),
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                CollisionContext.empty()
            )
        )
        val context = UseOnContext(player, interactionHand, hitResult)

        val be = level.getBlockEntity(context.clickedPos)
        if (be is ScriptableBlockEntity)
            player.openMenu(be)
        else return super.use(level, player, interactionHand)

        return InteractionResultHolder.success(player.getItemInHand(interactionHand))
    }
}