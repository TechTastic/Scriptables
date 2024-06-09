package io.github.techtastic.scriptables.api.lua

import java.util.function.Consumer

class Script {
    private val lines =
        mutableListOf<String>()

    fun newLine() =
        this.newLine("\n")

    fun newLine(line: String) =
        this.lines.add(line)

    fun getLine(line: Int) =
        this.lines[line]

    fun removeLine(line: Int) =
        this.lines.removeAt(line)

    fun getFormattedScript(lineEnd: String): String {
        var script = ""

        this.lines.forEach { line ->
            script += line + lineEnd
        }

        return script
    }

    fun getRunnableScript() =
        getFormattedScript("")

    fun getUploadableScript() =
        getFormattedScript("\n")

    companion object {
        fun fromFormattedScript(formatted: String): Script {
            val script = Script()

            val indexedNewLine = formatted.indexOf("\n")
            while (indexedNewLine != -1) {
                script.newLine(formatted.substring(0, indexedNewLine))
                formatted.removeRange(0, indexedNewLine + 2)
            }

            return script
        }
    }
}