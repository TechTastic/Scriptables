package io.github.techtastic.scriptables.screen

import com.mojang.blaze3d.systems.RenderSystem
import io.github.techtastic.scriptables.Scriptables
import io.github.techtastic.scriptables.api.lua.Script
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.CodeEditBox
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.network.chat.Component

class ScriptEditorScreen(val script: Script): Screen(Component.translatable("gui.scriptables.script_editor")) {
    private val TEXTURE = Scriptables.getWithModId("textures/gui/script_editor.png")
    private lateinit var codeEditor: CodeEditBox

    val imageWidth = 176
    val imageHeight = 222

    override fun init() {
        super.init()
        val x: Int = (width - imageWidth) / 2
        val y: Int = (height - imageHeight) / 2

        this.codeEditor = CodeEditBox(
            this.font,
            x + 43,
            y + 17,
            126,
            142,
            Component.literal("while true do\n    --your code here\nend"),
            Component.literal("Code Editor"),
            this.script
        )
        this.addRenderableWidget(this.codeEditor)

        //TOOD: Add buttons, errors, and console
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

    override fun render(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
        this.renderBackground(guiGraphics, i, j, f)
        super.render(guiGraphics, i, j, f)
    }
}