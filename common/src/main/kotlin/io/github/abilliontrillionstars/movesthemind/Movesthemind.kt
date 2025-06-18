package io.github.abilliontrillionstars.movesthemind

import net.minecraft.resources.ResourceLocation
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import io.github.abilliontrillionstars.movesthemind.config.MovesthemindConfig
import io.github.abilliontrillionstars.movesthemind.networking.MovesthemindNetworking
import io.github.abilliontrillionstars.movesthemind.registry.MovesthemindActions

object Movesthemind {
    const val MODID = "movesthemind"

    @JvmField
    val LOGGER: Logger = LogManager.getLogger(MODID)

    @JvmStatic
    fun id(path: String) = ResourceLocation(MODID, path)

    fun init() {
        MovesthemindConfig.init()
        initRegistries(
            MovesthemindActions,
        )
        MovesthemindNetworking.init()
    }
}
