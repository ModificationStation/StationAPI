package net.modificationstation.stationapi.api.server.event.network;

import lombok.RequiredArgsConstructor;
import net.minecraft.packet.handshake.HandshakeC2S;
import net.minecraft.server.network.PendingConnection;
import net.mine_diver.unsafeevents.Event;

@RequiredArgsConstructor
public class HandshakeSuccessEvent extends Event {

    public final PendingConnection pendingConnection;
    public final HandshakeC2S handshakePacket;

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
