package io.github.abilliontrillionstars.movesthemind.forge

import io.github.abilliontrillionstars.movesthemind.MovesthemindClient
import net.minecraftforge.client.ConfigScreenHandler.ConfigScreenFactory
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import thedarkcolour.kotlinforforge.forge.LOADING_CONTEXT

object ForgeMovesthemindClient {
    fun init(event: FMLClientSetupEvent) {
        MovesthemindClient.init()
        LOADING_CONTEXT.registerExtensionPoint(ConfigScreenFactory::class.java) {
            ConfigScreenFactory { _, parent -> MovesthemindClient.getConfigScreen(parent) }
        }
    }
}
