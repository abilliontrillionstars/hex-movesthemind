package io.github.abilliontrillionstars.movesthemind.fabric

import io.github.abilliontrillionstars.movesthemind.Movesthemind
import net.fabricmc.api.ModInitializer

object FabricMovesthemind : ModInitializer {
    override fun onInitialize() {
        Movesthemind.init()
    }
}
