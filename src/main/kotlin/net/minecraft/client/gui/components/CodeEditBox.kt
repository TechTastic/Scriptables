package net.minecraft.client.gui.components

import io.github.techtastic.scriptables.api.lua.Script
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.Util
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.MultilineTextField.StringView
import net.minecraft.client.gui.narration.NarratedElementType
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.renderer.RenderType
import net.minecraft.network.chat.Component
import net.minecraft.util.StringUtil
import java.util.*
import java.util.function.Consumer
import kotlin.math.max

@Environment(EnvType.CLIENT)
class CodeEditBox(val font: Font, x: Int, y: Int, width: Int, height: Int, val narration: Component, val placeholder: Component, val script: Script) : AbstractScrollWidget(x, y, width, height, narration) {
    val textField = object: MultilineTextField(font, this.width - this.totalInnerPadding()) {
        override fun insertText(string: String) {
            //TODO: Handle Gist, Pastebin, etc

            super.insertText(string)
        }
    }
    var focusedTime = Util.getMillis()

    init {
        this.setValue(this.script.getUploadableScript())
        this.textField.setCursorListener(this::scrollToCursor)
    }

    fun scrollToCursor() {
        var scrollAmount = this.scrollAmount()
        var textField = this.textField
        Objects.requireNonNull(this.font)
        val stringView = textField.getLineView((scrollAmount / 9.0).toInt())
        var lineNumber: Int
        if (this.textField.cursor() <= stringView.beginIndex) {
            lineNumber = this.textField.lineAtCursor
            Objects.requireNonNull(this.font)
            scrollAmount = (lineNumber * this.font.lineHeight).toDouble()
        } else {
            textField = this.textField
            val scrollEndAmount = scrollAmount + height.toDouble()
            Objects.requireNonNull(this.font)
            val stringView2 = textField.getLineView((scrollEndAmount / this.font.lineHeight).toInt() - 1)
            if (this.textField.cursor() > stringView2.endIndex) {
                lineNumber = this.textField.lineAtCursor
                Objects.requireNonNull(this.font)
                lineNumber = lineNumber * this.font.lineHeight - this.height
                Objects.requireNonNull(this.font)
                scrollAmount = (lineNumber + this.font.lineHeight + this.totalInnerPadding()).toDouble()
            }
        }

        this.setScrollAmount(scrollAmount)
    }

    override fun renderDecorations(guiGraphics: GuiGraphics) {
        super.renderDecorations(guiGraphics)

        if (this.textField.hasCharacterLimit()) {
            val limit = this.textField.characterLimit()
            val component = Component.translatable("gui.multiLineEditBox.character_limit",
                arrayOf(this.textField.value().length, limit))
            guiGraphics.drawString(
                this.font,
                component,
                this.getX() + this.width - this.font.width(component),
                this.getY() + this.height + 4,
                10526880
            )
        }
    }

    fun seekCursorScreen(d: Double, e: Double) {
        val f = d - this.x.toDouble() - innerPadding().toDouble()
        val g = e - this.y.toDouble() - innerPadding().toDouble() + this.scrollAmount()
        this.textField.seekCursorToPoint(f, g)
    }

    override fun setFocused(focus: Boolean) {
        super.setFocused(focus)
        if (focus) this.focusedTime = Util.getMillis()
    }

    fun renderBaseContent(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
        val string = textField.value()
        if (string.isEmpty() && !this.isFocused) {
            guiGraphics.drawStringWithBackdrop(
                this.font,
                this.placeholder,
                this.x + this.innerPadding(),
                this.y + this.innerPadding(),
                this.width - this.totalInnerPadding(), -857677600
            )
        } else {
            val k = textField.cursor()
            val bl = this.isFocused && (Util.getMillis() - this.focusedTime) / 300L % 2L == 0L
            val bl2 = k < string.length
            var l = 0
            var m = 0
            var n = this.y + this.innerPadding()

            var var10002: Int
            var var10004: Int
            val var12: Iterator<*> = textField.iterateLines().iterator()
            while (var12.hasNext()) {
                val stringView = var12.next() as StringView
                Objects.requireNonNull(this.font)
                val bl3 = this.withinContentAreaTopBottom(n, n + 9)
                if (bl && bl2 && k >= stringView.beginIndex() && k <= stringView.endIndex()) {
                    if (bl3) {
                        l = guiGraphics.drawString(
                            this.font, string.substring(stringView.beginIndex(), k),
                            this.x + this.innerPadding(), n, -2039584
                        ) - 1
                        var10002 = n - 1
                        val var10003 = l + 1
                        var10004 = n + 1
                        Objects.requireNonNull(this.font)
                        guiGraphics.fill(l, var10002, var10003, var10004 + 9, -3092272)
                        guiGraphics.drawString(this.font, string.substring(k, stringView.endIndex()), l, n, -2039584)
                    }
                } else {
                    if (bl3) {
                        l = guiGraphics.drawString(
                            this.font, string.substring(stringView.beginIndex(), stringView.endIndex()),
                            this.x + this.innerPadding(), n, -2039584
                        ) - 1
                    }

                    m = n
                }

                Objects.requireNonNull(this.font)
                n += 9
            }

            if (bl && !bl2) {
                Objects.requireNonNull(this.font)
                if (this.withinContentAreaTopBottom(m, m + 9)) {
                    guiGraphics.drawString(this.font, "_", l, m, -3092272)
                }
            }

            if (textField.hasSelection()) {
                val stringView2 = textField.selected
                val o = this.x + this.innerPadding()
                n = this.y + this.innerPadding()
                val var20: Iterator<*> = textField.iterateLines().iterator()

                while (var20.hasNext()) {
                    val stringView3 = var20.next() as StringView
                    if (stringView2.beginIndex() > stringView3.endIndex()) {
                        Objects.requireNonNull(this.font)
                        n += 9
                    } else {
                        if (stringView3.beginIndex() > stringView2.endIndex()) {
                            break
                        }

                        Objects.requireNonNull(this.font)
                        if (this.withinContentAreaTopBottom(n, n + 9)) {
                            val p = font.width(
                                string.substring(
                                    stringView3.beginIndex(),
                                    max(
                                        stringView2.beginIndex().toDouble(),
                                        stringView3.beginIndex().toDouble()
                                    ).toInt()
                                )
                            )
                            val q = if (stringView2.endIndex() > stringView3.endIndex()) {
                                width - this.innerPadding()
                            } else {
                                font.width(string.substring(stringView3.beginIndex(), stringView2.endIndex()))
                            }

                            var10002 = o + p
                            var10004 = o + q
                            Objects.requireNonNull(this.font)
                            this.renderHighlight(guiGraphics, var10002, n, var10004, n + 9)
                        }

                        Objects.requireNonNull(this.font)
                        n += 9
                    }
                }
            }
        }
    }

    fun renderHighlight(guiGraphics: GuiGraphics, i: Int, j: Int, k: Int, l: Int) =
        guiGraphics.fill(RenderType.guiTextHighlight(), i, j, k, l, -16776961)
    fun setValueListener(consumer: Consumer<String>) =
        this.textField.setValueListener(consumer)
    fun setValue(string: String) =
        this.textField.setValue(string)
    fun getValue(): String =
        this.textField.value()
    fun setCharacterLimit(limit: Int) =
        this.textField.setCharacterLimit(limit)

    override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {
        narrationElementOutput.add(NarratedElementType.USAGE, this.narration)
    }

    override fun getInnerHeight() = this.scrollRate().toInt() * textField.lineCount
    override fun scrollRate() = this.font.lineHeight.toDouble()
    override fun scrollbarVisible() = false
    override fun keyPressed(i: Int, j: Int, k: Int) = this.textField.keyPressed(i)

    override fun renderContents(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
        this.renderBaseContent(guiGraphics, i, j, f)
    }

    override fun mouseClicked(d: Double, e: Double, i: Int) =
        if (this.withinContentAreaPoint(d, e) && i == 0) {
            this.textField.setSelecting(Screen.hasShiftDown())
            this.seekCursorScreen(d, e)
            true
        } else super.mouseClicked(d, e, i)

    override fun mouseDragged(d: Double, e: Double, i: Int, f: Double, g: Double) =
        if (super.mouseDragged(d, e, i, f, g)) true
        else if (this.withinContentAreaPoint(d, e) && i == 0) {
            this.textField.setSelecting(true)
            this.seekCursorScreen(d, e)
            this.textField.setSelecting(Screen.hasShiftDown())
            true
        } else false

    override fun charTyped(c: Char, i: Int) =
        if (this.visible && this.isFocused && StringUtil.isAllowedChatCharacter(c)) {
            this.textField.insertText(c.toString())
            true
        } else false

    companion object {
        const val CURSOR_INSERT_WIDTH: Int = 1
        const val CURSOR_INSERT_COLOR: Int = -3092272
        const val CURSOR_APPEND_CHARACTER: String = "_"
        const val TEXT_COLOR: Int = -2039584
        const val PLACEHOLDER_TEXT_COLOR: Int = -857677600
        const val CURSOR_BLINK_INTERVAL_MS: Int = 300
    }
}