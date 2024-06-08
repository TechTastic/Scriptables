package io.github.techtastic.scriptables

import io.github.techtastic.scriptables.block.ScriptableBlockEntities
import io.github.techtastic.scriptables.block.ScriptableBlocks
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.ModInitializer
import net.minecraft.resources.ResourceLocation
import org.slf4j.LoggerFactory

object Scriptables : ModInitializer {
	val MOD_ID = "scriptables"
    val LOGGER = LoggerFactory.getLogger(MOD_ID)

	override fun onInitialize() {
		ScriptableBlocks.init()
		ScriptableBlockEntities.init()
	}

	class Client: ClientModInitializer {
		override fun onInitializeClient() {
		}
	}

	fun getWithModId(path: String) = ResourceLocation.fromNamespaceAndPath(MOD_ID, path)
}