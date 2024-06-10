package io.github.techtastic.scriptables

import io.github.techtastic.scriptables.api.ScriptablesAPI
import io.github.techtastic.scriptables.api.scriptable.IScriptable
import io.github.techtastic.scriptables.api.scriptable.IScriptableProvider
import io.github.techtastic.scriptables.block.ScriptableBlock
import io.github.techtastic.scriptables.block.ScriptableBlockEntity
import io.github.techtastic.scriptables.item.ScriptItem
import io.github.techtastic.scriptables.networking.ScriptablesNetworking
import io.github.techtastic.scriptables.screen.ScriptEditorScreen
import io.netty.buffer.ByteBuf
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.client.renderer.RenderType
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item.Properties
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.phys.shapes.CollisionContext
import org.slf4j.LoggerFactory
import java.util.*


object Scriptables : ModInitializer {
	val MOD_ID = "scriptables"
    val LOGGER = LoggerFactory.getLogger(MOD_ID)

	val TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, getWithModId("tab"), FabricItemGroup.builder()
		.title(Component.translatable("title.scriptables"))
		.icon { ItemStack(SCRIPT) }
		.displayItems { _, output ->
			output.accept(SCRIPT)
			output.accept(SCRIPTABLE_BLOCK_ITEM)
		}.build())
	val SCRIPT = Registry.register(BuiltInRegistries.ITEM, getWithModId("script"), ScriptItem(Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant()))
	val SCRIPTABLE_BLOCK = Registry.register(BuiltInRegistries.BLOCK, getWithModId("scriptable_block"), ScriptableBlock(BlockBehaviour.Properties.of()))
	val SCRIPTABLE_BLOCK_ITEM = Registry.register(BuiltInRegistries.ITEM, getWithModId("scriptable_block"), BlockItem(SCRIPTABLE_BLOCK, Properties().rarity(Rarity.EPIC).fireResistant()))
	val SCRIPTABLE_BLOCK_ENTITY = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, getWithModId("scriptable_block"), BlockEntityType.Builder.of(::ScriptableBlockEntity, SCRIPTABLE_BLOCK).build())

	override fun onInitialize() {
		ScriptablesAPI.registerScriptableProvider(DefaultScriptableProvider())

		ScriptablesNetworking.registerGlobalListeners()
	}

	class Client: ClientModInitializer {
		override fun onInitializeClient() {
			BlockRenderLayerMap.INSTANCE.putBlock(SCRIPTABLE_BLOCK, RenderType.translucent())
		}
	}

	fun getWithModId(path: String) = ResourceLocation.fromNamespaceAndPath(MOD_ID, path)

	class DefaultScriptableProvider: IScriptableProvider {
		override fun getScriptable(level: Level, player: Player, hand: InteractionHand): Optional<IScriptable> {
			if (level !is ServerLevel) return Optional.empty()

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
			val context = UseOnContext(player, hand, hitResult)

			val be = level.getBlockEntity(context.clickedPos)
			return if (be is IScriptable) Optional.of(be)
			else Optional.empty()
		}
	}
}