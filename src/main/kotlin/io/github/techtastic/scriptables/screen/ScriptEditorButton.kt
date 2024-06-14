package io.github.techtastic.scriptables.screen

import io.github.techtastic.scriptables.Scriptables.LOGGER
import io.github.techtastic.scriptables.Scriptables.getWithModId
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation

class ScriptEditorButton(
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    component: Component,
    onPress: OnPress,
    createNarration: CreateNarration,
    private val type: String
) : Button(x, y, width, height, component, onPress, createNarration) {
    val TEXTURE: ResourceLocation
    val PRESSED_TEXTURE: ResourceLocation

    var pressed = false

    init {
        this.TEXTURE = getWithModId("buttons/${type}_button")
        this.PRESSED_TEXTURE = getWithModId("buttons/${type}_button_hovered")
    }

    override fun onPress() {
        this.pressed = true
        super.onPress()
    }

    override fun onRelease(d: Double, e: Double) {
        this.pressed = false
        super.onRelease(d, e)
    }

    override fun renderWidget(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
        var sprite = if (this.pressed)
            PRESSED_TEXTURE
        else
            TEXTURE
        LOGGER.info("Button Type: $type")
        LOGGER.info("Button Texture: $sprite")
        guiGraphics.blitSprite(sprite, this.getX(), this.getY(), this.width, this.height)
    }
}