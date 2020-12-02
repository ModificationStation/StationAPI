package net.modificationstation.stationloader.api.server.event.network;

import net.minecraft.packet.handshake.HandshakeC2S;
import net.minecraft.server.network.PendingConnection;
import net.modificationstation.stationloader.api.common.event.SimpleEvent;

public interface HandleLogin {

    SimpleEvent<HandleLogin> EVENT = new SimpleEvent<>(HandleLogin.class, listeners ->
            (pendingConnection, arg) -> {
                for (HandleLogin event : listeners)
                    event.handleLogin(pendingConnection, arg);
            });

    void handleLogin(PendingConnection pendingConnection, HandshakeC2S arg);
}
