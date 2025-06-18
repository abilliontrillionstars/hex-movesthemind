package io.github.abilliontrillionstars.movesthemind.forge

import dev.architectury.platform.forge.EventBuses
import io.github.abilliontrillionstars.movesthemind.Movesthemind
import net.minecraft.data.DataProvider
import net.minecraft.data.DataProvider.Factory
import net.minecraft.data.PackOutput
import net.minecraftforge.data.event.GatherDataEvent
import net.minecraftforge.fml.common.Mod
import thedarkcolour.kotlinforforge.forge.MOD_BUS

@Mod(Movesthemind.MODID)
class MovesthemindForge {
    init {
        MOD_BUS.apply {
            EventBuses.registerModEventBus(Movesthemind.MODID, this)
            addListener(ForgeMovesthemindClient::init)
            addListener(::gatherData)
        }
        Movesthemind.init()
    }

    private fun gatherData(event: GatherDataEvent) {
        event.apply {
            // TODO: add datagen providers here
        }
    }
}

fun <T : DataProvider> GatherDataEvent.addProvider(run: Boolean, factory: (PackOutput) -> T) =
    generator.addProvider(run, Factory { factory(it) })
