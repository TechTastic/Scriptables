package io.github.techtastic.scriptables.screen

import net.minecraft.client.gui.screens.Screen
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.network.chat.Component

class ScriptingScreen(var script: List<String>, var logs: List<Pair<Boolean, String>>): Screen(Component.literal("Script Editor")) {
    fun onSave() {
        this.minecraft?.player?.uuid?.let { uuid ->
            val data = CompoundTag()
            data.putUUID("scriptables\$uuid", uuid)
            val scriptTag = ListTag()
            for (lineNumber in 0..<this.script.size) {
                val tag = CompoundTag()
                tag.putString("scriptables\$line", this.script[lineNumber])
                scriptTag[lineNumber] = tag
            }
            data.put("scriptables\$script", scriptTag)

            //TODO: Encode To FriendlyByteBuf, send C2S packet
        }
    }

    fun onSnippet() {
        this.minecraft?.player?.uuid?.let { uuid ->
            val data = CompoundTag()
            data.putUUID("scriptables\$uuid", uuid)
            data.put("scriptables\$script", TODO("Snippet Here"))

            //TODO: Encode To FriendlyByteBuf, send C2S packet
        }
    }
}