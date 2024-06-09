package io.github.techtastic.scriptables.screen

import com.mojang.blaze3d.systems.RenderSystem
import io.github.techtastic.scriptables.Scriptables
import io.github.techtastic.scriptables.api.lua.Script
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.network.chat.Component


class ScriptEditorScreen(script: Script): Screen(Component.translatable("gui.scriptables.script_editor")) {
    private val TEXTURE = Scriptables.getWithModId("textures/gui/script_editor.png")
    val imageWidth = 219
    val imageHeight = 222
    val titleLabelX = 8
    val titleLabelY = 6

    override fun render(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
        super.render(guiGraphics, i, j, f)

        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false)
    }

    override fun renderBackground(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
        super.renderBackground(guiGraphics, i, j, f)
        RenderSystem.setShader(GameRenderer::getPositionTexShader)
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
        RenderSystem.setShaderTexture(0, TEXTURE)
        val x: Int = (width - imageWidth) / 2
        val y: Int = (height - imageHeight) / 2

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight)
    }
}