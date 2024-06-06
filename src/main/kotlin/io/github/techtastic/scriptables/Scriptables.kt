package io.github.techtastic.scriptables

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

object Scriptables : ModInitializer {
    private val logger = LoggerFactory.getLogger("scriptables")

	override fun onInitialize() {
	}

	class Client: ClientModInitializer {
		override fun onInitializeClient() {
		}

	}
}