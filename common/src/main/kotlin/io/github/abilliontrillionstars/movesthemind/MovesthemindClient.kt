package io.github.abilliontrillionstars.movesthemind

import io.github.abilliontrillionstars.movesthemind.config.MovesthemindConfig
import io.github.abilliontrillionstars.movesthemind.config.MovesthemindConfig.GlobalConfig
import me.shedaniel.autoconfig.AutoConfig
import net.minecraft.client.gui.screens.Screen

object MovesthemindClient {
    fun init() {
        MovesthemindConfig.initClient()
    }

    fun getConfigScreen(parent: Screen): Screen {
        return AutoConfig.getConfigScreen(GlobalConfig::class.java, parent).get()
    }
}
