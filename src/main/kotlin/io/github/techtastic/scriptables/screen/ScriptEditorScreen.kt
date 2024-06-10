package io.github.techtastic.scriptables.screen

import com.mojang.blaze3d.systems.RenderSystem
import io.github.techtastic.scriptables.Scriptables
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.CodeEditBox
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class ScriptEditorScreen(menu: ScriptEditorMenu, inventory: Inventory, component: Component): AbstractContainerScreen<ScriptEditorMenu>(menu, object: Inventory(inventory.player) {
        override fun getDisplayName() = Component.empty()
    }, component) {
    private val TEXTURE = Scriptables.getWithModId("textures/gui/script_editor.png")
    private lateinit var codeEditor: CodeEditBox

    init {
        this.imageWidth = 176
        this.imageHeight = 222
    }

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
            this.menu.getScript()
        )
        this.addRenderableWidget(this.codeEditor)
    }

    override fun render(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
        this.renderBackground(guiGraphics, i, j, f)
        super.render(guiGraphics, i, j, f)
        this.renderTooltip(guiGraphics, i, j)
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