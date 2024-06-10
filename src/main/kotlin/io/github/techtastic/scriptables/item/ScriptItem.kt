package io.github.techtastic.scriptables.item

import io.github.techtastic.scriptables.Scriptables.LOGGER
import io.github.techtastic.scriptables.api.ScriptablesAPI
import io.github.techtastic.scriptables.api.lua.Script
import io.github.techtastic.scriptables.screen.ScriptEditorScreen
import kotlinx.coroutines.handleCoroutineException
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
        val scriptable = ScriptablesAPI.findScriptable(level, player, interactionHand) ?: return super.use(level, player, interactionHand)
        // TODO: This should be a S2C packet tbh
        Minecraft.getInstance().setScreen(ScriptEditorScreen(scriptable.getScript()))

        return super.use(level, player, interactionHand)
    }
}