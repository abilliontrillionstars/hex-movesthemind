package io.github.abilliontrillionstars.movesthemind.networking.handler

import dev.architectury.networking.NetworkManager.PacketContext
import io.github.abilliontrillionstars.movesthemind.config.MovesthemindConfig
import io.github.abilliontrillionstars.movesthemind.networking.msg.*

fun MovesthemindMessageS2C.applyOnClient(ctx: PacketContext) = ctx.queue {
    when (this) {
        is MsgSyncConfigS2C -> {
            MovesthemindConfig.onSyncConfig(serverConfig)
        }

        // add more client-side message handlers here
    }
}
