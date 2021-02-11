package net.modificationstation.stationapi.api.server.event.network;

import net.minecraft.packet.handshake.HandshakeC2S;
import net.minecraft.server.network.PendingConnection;
import net.modificationstation.stationapi.api.common.event.GameEventOld;

import java.util.function.Consumer;

public interface HandleLogin {

    GameEventOld<HandleLogin> EVENT = new GameEventOld<>(HandleLogin.class,
            listeners ->
                    (pendingConnection, handshakePacket) -> {
                        for (HandleLogin listener : listeners)
                            listener.handleLogin(pendingConnection, handshakePacket);
                    },
            (Consumer<GameEventOld<HandleLogin>>) handleLogin ->
                    handleLogin.register((pendingConnection, handshakePacket) -> GameEventOld.EVENT_BUS.post(new Data(pendingConnection, handshakePacket)))
    );

    void handleLogin(PendingConnection pendingConnection, HandshakeC2S handshakePacket);

    final class Data extends GameEventOld.Data<HandleLogin> {

        public final PendingConnection pendingConnection;
        public final HandshakeC2S handshakePacket;

        private Data(PendingConnection pendingConnection, HandshakeC2S handshakePacket) {
            super(EVENT);
            this.pendingConnection = pendingConnection;
            this.handshakePacket = handshakePacket;
        }
    }
}
