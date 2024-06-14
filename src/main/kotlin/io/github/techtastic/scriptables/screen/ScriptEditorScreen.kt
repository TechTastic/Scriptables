package io.github.techtastic.scriptables.screen

import com.mojang.blaze3d.systems.RenderSystem
import io.github.techtastic.scriptables.Scriptables
import io.github.techtastic.scriptables.Scriptables.LOGGER
import io.github.techtastic.scriptables.Scriptables.getWithModId
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.*
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class ScriptEditorScreen(menu: ScriptEditorMenu, inventory: Inventory, component: Component) :
    AbstractContainerScreen<ScriptEditorMenu>(menu, inventory, component) {
    private val TEXTURE = getWithModId("textures/gui/script_editor.png")
    private lateinit var codeEditor: CodeEditBox
    private lateinit var errorShown: MultilineTextField
    private lateinit var console: EditBox

    init {
        this.imageWidth = 199
        this.imageHeight = 222
    }

    override fun init() {
        super.init()
        val x: Int = (width - imageWidth) / 2
        val y: Int = (height - imageHeight) / 2

        this.codeEditor = CodeEditBox(
            this.font,
            x + 7,
            y + 17,
            162,
            108,
            Component.literal("Code Editor"),
            Component.literal("while true do\n    --your code here\nend")
        )
        this.codeEditor.setValue(this.menu.getFormattedScript())
        this.addRenderableWidget(this.codeEditor)

        //TODO: Upload Button: 177, 31
        this.addRenderableWidget(
            ScriptEditorButton(x + 176, y + 32, 16, 16, Component.empty(), { button -> LOGGER.info("Upload Attmpted!") }, { Component.empty() }, "upload")
        )
        //TODO: Save Button: 177, 63
        this.addRenderableWidget(
            ScriptEditorButton(x + 176, y + 64, 16, 16, Component.empty(), { button -> LOGGER.info("Save Attmpted!") }, { Component.empty() }, "save")
        )
        //TODO: File Button: 177, 79
        this.addRenderableWidget(
            ScriptEditorButton(x + 176, y + 96, 16, 16, Component.empty(), { button -> LOGGER.info("File Opening Attmpted!") }, { Component.empty() }, "file")
        )
        //TODO: Error Multiline: 7, 139 by 162 x 65
        //TODO: Console Input: 7, 204 by 162 x 11
    }

    override fun renderBg(guiGraphics: GuiGraphics, f: Float, i: Int, j: Int) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader)
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
        RenderSystem.setShaderTexture(0, TEXTURE)
        val x: Int = (width - imageWidth) / 2
        val y: Int = (height - imageHeight) / 2

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight)
    }
}