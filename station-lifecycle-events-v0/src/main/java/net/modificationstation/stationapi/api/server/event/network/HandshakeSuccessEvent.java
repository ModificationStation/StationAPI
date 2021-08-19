package net.modificationstation.stationapi.api.server.event.network;

import lombok.RequiredArgsConstructor;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.packet.login.LoginRequest0x1Packet;
import net.minecraft.server.network.ServerPacketHandler;

@RequiredArgsConstructor
public class HandshakeSuccessEvent extends Event {

    public final ServerPacketHandler pendingConnection;
    public final LoginRequest0x1Packet handshakePacket;

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
