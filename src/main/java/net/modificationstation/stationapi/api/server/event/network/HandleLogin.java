package net.modificationstation.stationapi.api.server.event.network;

import lombok.RequiredArgsConstructor;
import net.minecraft.packet.handshake.HandshakeC2S;
import net.minecraft.server.network.PendingConnection;
import net.modificationstation.stationapi.api.common.event.Event;
import net.modificationstation.stationapi.api.common.event.GameEventOld;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class HandleLogin extends Event {

    public final PendingConnection pendingConnection;
    public final HandshakeC2S handshakePacket;
}
