package net.modificationstation.stationapi.api.server.event.network;

import lombok.RequiredArgsConstructor;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.entity.player.ServerPlayer;
import net.minecraft.packet.login.LoginRequest0x1Packet;

@RequiredArgsConstructor
public class PlayerLoginEvent extends Event {

    public final LoginRequest0x1Packet loginPacket;
    public final ServerPlayer player;

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
