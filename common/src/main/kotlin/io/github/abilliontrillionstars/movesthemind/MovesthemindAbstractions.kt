@file:JvmName("MovesthemindAbstractions")

package io.github.abilliontrillionstars.movesthemind

import dev.architectury.injectables.annotations.ExpectPlatform
import io.github.abilliontrillionstars.movesthemind.registry.MovesthemindRegistrar

fun initRegistries(vararg registries: MovesthemindRegistrar<*>) {
    for (registry in registries) {
        initRegistry(registry)
    }
}

@ExpectPlatform
fun <T : Any> initRegistry(registrar: MovesthemindRegistrar<T>) {
    throw AssertionError()
}
