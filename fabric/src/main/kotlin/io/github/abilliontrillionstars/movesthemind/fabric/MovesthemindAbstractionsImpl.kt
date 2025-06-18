@file:JvmName("MovesthemindAbstractionsImpl")

package io.github.abilliontrillionstars.movesthemind.fabric

import io.github.abilliontrillionstars.movesthemind.registry.MovesthemindRegistrar
import net.minecraft.core.Registry

fun <T : Any> initRegistry(registrar: MovesthemindRegistrar<T>) {
    val registry = registrar.registry
    registrar.init { id, value -> Registry.register(registry, id, value) }
}
