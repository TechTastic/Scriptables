package io.github.techtastic.scriptables.item

import io.github.techtastic.scriptables.Scriptables
import io.github.techtastic.scriptables.Scriptables.LOGGER
import io.github.techtastic.scriptables.Scriptables.MOD_ID
import io.github.techtastic.scriptables.item.custom.ScripterItem
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.Item

object ScriptableItems {
    val SCRIPTER = registerItem("scripter", ScripterItem(Item.Properties()))

    fun init() {
        LOGGER.info("Registering $MOD_ID Items!")
    }

    private fun registerItem(id: String, item: Item) =
        Registry.register(BuiltInRegistries.ITEM, Scriptables.getWithModId(id), item)
}