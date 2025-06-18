package io.github.abilliontrillionstars.movesthemind.networking.msg

import io.github.abilliontrillionstars.movesthemind.config.MovesthemindConfig
import net.minecraft.network.FriendlyByteBuf

data class MsgSyncConfigS2C(val serverConfig: MovesthemindConfig.ServerConfig) : MovesthemindMessageS2C {
    companion object : MovesthemindMessageCompanion<MsgSyncConfigS2C> {
        override val type = MsgSyncConfigS2C::class.java

        override fun decode(buf: FriendlyByteBuf) = MsgSyncConfigS2C(
            serverConfig = MovesthemindConfig.ServerConfig.decode(buf),
        )

        override fun MsgSyncConfigS2C.encode(buf: FriendlyByteBuf) {
            serverConfig.encode(buf)
        }
    }
}
