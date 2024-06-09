package io.github.techtastic.scriptables.api.lua

import net.minecraft.nbt.CompoundTag

abstract class Script {
    private val lines =
        mutableListOf<String>()

    /**
     * Adds a new empty line
     */
    fun newLine() =
        this.newLine("")

    /**
     * Adds a new line with custom text
     */
    fun newLine(line: String) =
        this.lines.add(line)

    /**
     * Grabs a line based on line number
     */
    fun getLine(line: Int) =
        this.lines[line]

    /**
     * Removes a line based on line number
     */
    fun removeLine(line: Int) =
        this.lines.removeAt(line)

    /**
     * Creates a formatted String appending the lineEnd to each line
     */
    fun getFormattedScript(lineEnd: String): String {
        var script = ""

        this.lines.forEach { line ->
            script += line + lineEnd
        }

        return script
    }

    /**
     * Gets a formatted String used to run in the Lua Sandbox
     */
    fun getRunnableScript() =
        getFormattedScript("")

    /**
     * Gets a formatted String for uploading
     */
    fun getUploadableScript() =
        getFormattedScript("\n")

    /**
     * Gets a Script from a formatted String (particularly from Script.getUploadableScript())
     */
    fun fromFormattedScript(formatted: String): Script {
        this.lines.clear()
        val indexedNewLine = formatted.indexOf("\n")
        while (indexedNewLine != -1) {
            this.newLine(formatted.substring(0, indexedNewLine))
            formatted.removeRange(0, indexedNewLine + 2)
        }

        return this
    }

    /**
     * Called when a Script is saved
     */
    abstract fun reload()

    fun saveScript(tag: CompoundTag) =
        tag.putString("scriptables\$script", this.getUploadableScript())

    fun loadScript(tag: CompoundTag) {
        this.fromFormattedScript(tag.getString("scriptables\$script"))
    }
}