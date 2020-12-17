package net.modificationstation.stationloader.api.server.event.network;

import lombok.Getter;
import net.minecraft.packet.handshake.HandshakeC2S;
import net.minecraft.server.network.PendingConnection;
import net.modificationstation.stationloader.api.common.event.GameEvent;

import java.util.function.Consumer;

public interface HandleLogin {

    GameEvent<HandleLogin> EVENT = new GameEvent<>(HandleLogin.class,
            listeners ->
                    (pendingConnection, handshakePacket) -> {
                        for (HandleLogin listener : listeners)
                            listener.handleLogin(pendingConnection, handshakePacket);
                    },
            (Consumer<GameEvent<HandleLogin>>) handleLogin ->
                    handleLogin.register((pendingConnection, handshakePacket) -> GameEvent.EVENT_BUS.post(new Data(pendingConnection, handshakePacket)))
    );

    void handleLogin(PendingConnection pendingConnection, HandshakeC2S handshakePacket);

    final class Data extends GameEvent.Data<HandleLogin> {

        @Getter
        private final PendingConnection pendingConnection;
        @Getter
        private final HandshakeC2S handshakePacket;

        private Data(PendingConnection pendingConnection, HandshakeC2S handshakePacket) {
            super(EVENT);
            this.pendingConnection = pendingConnection;
            this.handshakePacket = handshakePacket;
        }
    }
}
