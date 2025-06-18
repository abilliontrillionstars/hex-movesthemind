package io.github.abilliontrillionstars.movesthemind.fabric

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import io.github.abilliontrillionstars.movesthemind.MovesthemindClient

object FabricMovesthemindModMenu : ModMenuApi {
    override fun getModConfigScreenFactory() = ConfigScreenFactory(MovesthemindClient::getConfigScreen)
}
