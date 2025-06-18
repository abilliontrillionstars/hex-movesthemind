package io.github.abilliontrillionstars.movesthemind.networking

import dev.architectury.networking.NetworkChannel
import io.github.abilliontrillionstars.movesthemind.Movesthemind
import io.github.abilliontrillionstars.movesthemind.networking.msg.MovesthemindMessageCompanion

object MovesthemindNetworking {
    val CHANNEL: NetworkChannel = NetworkChannel.create(Movesthemind.id("networking_channel"))

    fun init() {
        for (subclass in MovesthemindMessageCompanion::class.sealedSubclasses) {
            subclass.objectInstance?.register(CHANNEL)
        }
    }
}
