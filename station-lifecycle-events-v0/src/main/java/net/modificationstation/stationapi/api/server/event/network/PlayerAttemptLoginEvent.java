package net.modificationstation.stationapi.api.server.event.network;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.mine_diver.unsafeevents.event.EventPhases;
import net.minecraft.packet.login.LoginRequest0x1Packet;
import net.minecraft.server.network.ServerPacketHandler;
import net.modificationstation.stationapi.api.StationAPI;

@SuperBuilder
@EventPhases(StationAPI.INTERNAL_PHASE)
public class PlayerAttemptLoginEvent extends Event {
    public final ServerPacketHandler serverPacketHandler;
    public final LoginRequest0x1Packet loginRequestPacket;
}
