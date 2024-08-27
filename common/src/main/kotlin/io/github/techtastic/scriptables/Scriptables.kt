package io.github.techtastic.scriptables

import io.github.techtastic.scriptables.block.SBlockEntities
import io.github.techtastic.scriptables.block.SBlocks

object Scriptables {
    const val MOD_ID = "scriptables"

    fun init() {
        SBlocks.register()
        SBlockEntities.register()
    }
}