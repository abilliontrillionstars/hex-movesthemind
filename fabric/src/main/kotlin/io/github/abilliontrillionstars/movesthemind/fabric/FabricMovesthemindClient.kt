package io.github.abilliontrillionstars.movesthemind.fabric

import io.github.abilliontrillionstars.movesthemind.MovesthemindClient
import net.fabricmc.api.ClientModInitializer

object FabricMovesthemindClient : ClientModInitializer {
    override fun onInitializeClient() {
        MovesthemindClient.init()
    }
}
