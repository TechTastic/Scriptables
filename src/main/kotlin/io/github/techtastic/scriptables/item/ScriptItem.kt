package io.github.techtastic.scriptables.item

import io.github.techtastic.scriptables.Scriptables.LOGGER
import io.github.techtastic.scriptables.api.lua.Script
import io.github.techtastic.scriptables.screen.ScriptEditorScreen
import net.minecraft.client.Minecraft
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.Level
import net.minecraft.world.phys.shapes.CollisionContext

class ScriptItem(properties: Properties) : Item(properties) {
    val script = object: Script() {
        override fun reload() { }
    }

    override fun use(
        level: Level,
        player: Player,
        interactionHand: InteractionHand
    ): InteractionResultHolder<ItemStack> {
        val hitResult = level.clip(ClipContext(
            player.eyePosition,
            player.eyePosition.add(player.lookAngle.multiply(player.blockInteractionRange(), player.blockInteractionRange(), player.blockInteractionRange())),
            ClipContext.Block.COLLIDER,
            ClipContext.Fluid.NONE,
            CollisionContext.empty()
        ))

        val context = UseOnContext(player, interactionHand, hitResult)
        LOGGER.info(context.clickedPos.toString())
        LOGGER.info(context.clickLocation.toString())

        //TODO: Detect my Scriptable stuff
        //TODO: Else open Script Editor

        Minecraft.getInstance().setScreen(ScriptEditorScreen(this.script))

        return super.use(level, player, interactionHand)
    }
}