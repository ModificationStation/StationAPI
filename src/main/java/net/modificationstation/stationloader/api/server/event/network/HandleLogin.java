package net.modificationstation.stationloader.api.server.event.network;

import lombok.Getter;
import net.minecraft.packet.handshake.HandshakeC2S;
import net.minecraft.server.network.PendingConnection;
import net.modificationstation.stationloader.api.common.event.SimpleEvent;

import java.util.function.Consumer;

public interface HandleLogin {

    @SuppressWarnings("UnstableApiUsage")
    SimpleEvent<HandleLogin> EVENT = new SimpleEvent<>(HandleLogin.class,
            listeners ->
                    (pendingConnection, handshakePacket) -> {
                        for (HandleLogin listener : listeners)
                            listener.handleLogin(pendingConnection, handshakePacket);
                    }, (Consumer<SimpleEvent<HandleLogin>>) handleLogin ->
            handleLogin.register((pendingConnection, handshakePacket) -> SimpleEvent.EVENT_BUS.post(new Data(pendingConnection, handshakePacket)))
    );

    void handleLogin(PendingConnection pendingConnection, HandshakeC2S handshakePacket);

    final class Data extends SimpleEvent.Data<HandleLogin> {

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
