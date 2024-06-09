package io.github.techtastic.scriptables

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.ModInitializer
import net.minecraft.resources.ResourceLocation
import org.slf4j.LoggerFactory

object Scriptables : ModInitializer {
	val MOD_ID = "scriptables"
    val LOGGER = LoggerFactory.getLogger(MOD_ID)

	override fun onInitialize() {

	}

	class Client: ClientModInitializer {
		override fun onInitializeClient() {
		}
	}

	fun getWithModId(path: String) = ResourceLocation.fromNamespaceAndPath(MOD_ID, path)
}