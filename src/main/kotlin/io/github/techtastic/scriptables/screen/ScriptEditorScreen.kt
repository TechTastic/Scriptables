package io.github.techtastic.scriptables.screen

import com.mojang.blaze3d.platform.InputConstants
import com.mojang.blaze3d.systems.RenderSystem
import io.github.techtastic.scriptables.Scriptables.LOGGER
import io.github.techtastic.scriptables.Scriptables.getWithModId
import io.github.techtastic.scriptables.block.ScriptableBlockEntity
import io.github.techtastic.scriptables.networking.packet.ScriptableBlockRunnablePayload
import io.github.techtastic.scriptables.util.NamelessInventory
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.ChatFormatting
import net.minecraft.client.KeyMapping
import net.minecraft.client.KeyboardHandler
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.*
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.player.KeyboardInput
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TextColor
import net.minecraft.world.entity.player.Inventory
import java.util.function.Consumer
import java.util.function.Supplier


class ScriptEditorScreen(menu: ScriptEditorMenu, inventory: Inventory, component: Component) :
    AbstractContainerScreen<ScriptEditorMenu>(menu, NamelessInventory(inventory.player), component) {
    private val TEXTURE = getWithModId("textures/gui/script_editor.png")
    private lateinit var codeEditor: CodeEditBox
    private lateinit var logField: MultiLineTextWidget
    private lateinit var console: EditBox
    private lateinit var uploadButton: ScriptEditorButton
    private lateinit var saveButton: ScriptEditorButton
    private lateinit var fileButton: ScriptEditorButton

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
            Component.literal("-- code here")
        )
        this.codeEditor.setValue(ScriptableBlockEntity.getFormattedScript(this.menu.script))
        this.addRenderableWidget(this.codeEditor)

        this.uploadButton = this.addRenderableWidget(
            ScriptEditorButton(x + 176, y + 32, 16, 16, Component.empty(), { button -> minecraft?.gameMode?.handleInventoryButtonClick(this.menu.syncId, 0) }, "upload")
        )

        this.saveButton = this.addRenderableWidget(
            ScriptEditorButton(x + 176, y + 64, 16, 16, Component.empty(), { button -> minecraft?.gameMode?.handleInventoryButtonClick(this.menu.syncId, 1) }, "save")
        )

        this.fileButton = this.addRenderableWidget(
            ScriptEditorButton(x + 176, y + 96, 16, 16, Component.empty(), { button -> minecraft?.gameMode?.handleInventoryButtonClick(this.menu.syncId, 2) }, "file")
        )

        this.logField = MultiLineTextWidget(x + 9, y + 141, Component.literal("Logs"), this.font)
        this.logField.setMaxWidth(158)
        this.logField.setMaxRows(61)
        this.addRenderableWidget(this.logField)

        this.console = object: EditBox(this.font, x + 7, y + 204, 162, 11, Component.literal("Console")) {
            init {
                this.setMaxLength(256)
            }
        }
        this.addRenderableWidget(this.console)
    }

    override fun renderBg(guiGraphics: GuiGraphics, f: Float, i: Int, j: Int) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader)
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
        RenderSystem.setShaderTexture(0, TEXTURE)
        val x: Int = (width - imageWidth) / 2
        val y: Int = (height - imageHeight) / 2

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight)
    }

    override fun containerTick() {
        super.containerTick()

        val latest = this.menu.getLog().lastOrNull { pair -> pair.second.isNotEmpty() }
        if (latest == null) {
            this.logField.message = Component.literal("Code not ran yet!").withStyle(ChatFormatting.GOLD)
            return
        }
        this.logField.message = Component.literal(latest.second.replace("\t", "  ") + "\n").withStyle(if (latest.first) ChatFormatting.WHITE else ChatFormatting.RED)
    }

    override fun keyPressed(i: Int, j: Int, k: Int): Boolean {
        when (i) {
            InputConstants.KEY_RETURN -> {
                if (this.console.isFocused) {
                    minecraft?.gameMode?.handleInventoryButtonClick(menu.syncId, 3)
                    ClientPlayNetworking.send(ScriptableBlockRunnablePayload(this.menu.data.pos, this.console.value))
                    this.console.value = ""
                }
            }
        }
        return super.keyPressed(i, j, k)
    }

    class ScriptEditorButton(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        component: Component,
        val onPress: Consumer<ScriptEditorButton>,
        private val type: String
    ) : AbstractButton(x, y, width, height, component) {
        var ticks = 0f
        var pressed = false

        override fun onClick(d: Double, e: Double) {
            this.pressed = true
        }

        override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {
            this.defaultButtonNarrationText(narrationElementOutput)
        }

        override fun mouseDragged(d: Double, e: Double, i: Int, f: Double, g: Double) = false

        override fun onPress() {
            this.onPress.accept(this)
        }

        override fun renderWidget(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
            val sprite =
                if (this.pressed) getWithModId("buttons/${type}_button_hovered")
                else getWithModId("buttons/${type}_button")

            guiGraphics.blitSprite(sprite, this.x, this.y, this.width, this.height)

            this.ticks += ticks + (1 * f)
            if (this.pressed && ticks >= 10f) {
                ticks = -1f
                pressed = false
            }
        }
    }
}