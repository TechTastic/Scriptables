package io.github.techtastic.scriptables

import io.github.techtastic.scriptables.block.ScriptableBlock
import io.github.techtastic.scriptables.block.ScriptableBlockEntity
import io.github.techtastic.scriptables.item.ScriptItem
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.client.renderer.RenderBuffers
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.block.BlockRenderDispatcher
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item.Properties
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockBehaviour
import org.slf4j.LoggerFactory

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
	}

	class Client: ClientModInitializer {
		override fun onInitializeClient() {
			BlockRenderLayerMap.INSTANCE.putBlock(SCRIPTABLE_BLOCK, RenderType.translucent())
		}
	}

	fun getWithModId(path: String) = ResourceLocation.fromNamespaceAndPath(MOD_ID, path)
}