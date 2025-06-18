package io.github.abilliontrillionstars.movesthemind.networking.msg

import dev.architectury.networking.NetworkChannel
import dev.architectury.networking.NetworkManager.PacketContext
import io.github.abilliontrillionstars.movesthemind.Movesthemind
import io.github.abilliontrillionstars.movesthemind.networking.MovesthemindNetworking
import io.github.abilliontrillionstars.movesthemind.networking.handler.applyOnClient
import io.github.abilliontrillionstars.movesthemind.networking.handler.applyOnServer
import net.fabricmc.api.EnvType
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.server.level.ServerPlayer
import java.util.function.Supplier

sealed interface MovesthemindMessage

sealed interface MovesthemindMessageC2S : MovesthemindMessage {
    fun sendToServer() {
        MovesthemindNetworking.CHANNEL.sendToServer(this)
    }
}

sealed interface MovesthemindMessageS2C : MovesthemindMessage {
    fun sendToPlayer(player: ServerPlayer) {
        MovesthemindNetworking.CHANNEL.sendToPlayer(player, this)
    }

    fun sendToPlayers(players: Iterable<ServerPlayer>) {
        MovesthemindNetworking.CHANNEL.sendToPlayers(players, this)
    }
}

sealed interface MovesthemindMessageCompanion<T : MovesthemindMessage> {
    val type: Class<T>

    fun decode(buf: FriendlyByteBuf): T

    fun T.encode(buf: FriendlyByteBuf)

    fun apply(msg: T, supplier: Supplier<PacketContext>) {
        val ctx = supplier.get()
        when (ctx.env) {
            EnvType.SERVER, null -> {
                Movesthemind.LOGGER.debug("Server received packet from {}: {}", ctx.player.name.string, this)
                when (msg) {
                    is MovesthemindMessageC2S -> msg.applyOnServer(ctx)
                    else -> Movesthemind.LOGGER.warn("Message not handled on server: {}", msg::class)
                }
            }
            EnvType.CLIENT -> {
                Movesthemind.LOGGER.debug("Client received packet: {}", this)
                when (msg) {
                    is MovesthemindMessageS2C -> msg.applyOnClient(ctx)
                    else -> Movesthemind.LOGGER.warn("Message not handled on client: {}", msg::class)
                }
            }
        }
    }

    fun register(channel: NetworkChannel) {
        channel.register(type, { msg, buf -> msg.encode(buf) }, ::decode, ::apply)
    }
}
