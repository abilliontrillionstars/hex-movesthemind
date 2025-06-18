@file:JvmName("MovesthemindAbstractionsImpl")

package io.github.abilliontrillionstars.movesthemind.forge

import io.github.abilliontrillionstars.movesthemind.registry.MovesthemindRegistrar
import net.minecraftforge.registries.RegisterEvent
import thedarkcolour.kotlinforforge.forge.MOD_BUS

fun <T : Any> initRegistry(registrar: MovesthemindRegistrar<T>) {
    MOD_BUS.addListener { event: RegisterEvent ->
        event.register(registrar.registryKey) { helper ->
            registrar.init(helper::register)
        }
    }
}
