package io.github.techtastic.scriptables.screen

import com.mojang.blaze3d.systems.RenderSystem
import io.github.techtastic.scriptables.Scriptables
import io.github.techtastic.scriptables.Scriptables.LOGGER
import io.github.techtastic.scriptables.Scriptables.getWithModId
import io.github.techtastic.scriptables.api.lua.Script
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.*
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.network.chat.Component

class ScriptEditorScreen(val script: Script): Screen(Component.translatable("gui.scriptables.script_editor")) {
    private val TEXTURE = Scriptables.getWithModId("textures/gui/script_editor.png")
    private lateinit var codeEditor: CodeEditBox
    private lateinit var errorShown: MultilineTextField
    private lateinit var console: EditBox

    val imageWidth = 199
    val imageHeight = 222

    override fun init() {
        super.init()
        val x: Int = (width - imageWidth) / 2
        val y: Int = (height - imageHeight) / 2

        this.codeEditor = CodeEditBox(
            this.font,
            x + 7,
            y + 17,
            162,
            198,
            Component.literal("while true do\n    --your code here\nend"),
            Component.literal("Code Editor"),
            this.script
        )
        this.addRenderableWidget(this.codeEditor)

        //TODO: Upload Button: 177, 31
        this.addRenderableWidget(
            ImageButton(x + 177, y + 31, 16, 16, WidgetSprites(getWithModId("buttoms/upload_button"), getWithModId("buttoms/upload_button_hovered"))
            ) { button -> LOGGER.info("File trying ot be uploaded!") }
        )
        //TODO: Save Button: 177, 63
        this.addRenderableWidget(
            ImageButton(x + 177, y + 63, 16, 16, WidgetSprites(getWithModId("buttoms/save_button"), getWithModId("buttoms/save_button_hovered"))
            ) { button -> LOGGER.info("File trying ot be uploaded!") }
        )
        //TODO: File Button: 177, 79
        //TODO: Error Multiline: 7, 139 by 162 x 65
        //TODO: Console Input: 7, 204 by 162 x 11
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