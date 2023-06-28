package net.modificationstation.stationapi.api.client.event.network;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.mine_diver.unsafeevents.event.Cancelable;
import net.mine_diver.unsafeevents.event.EventPhases;
import net.minecraft.network.ClientPlayNetworkHandler;
import net.minecraft.packet.login.LoginRequest0x1Packet;
import net.modificationstation.stationapi.api.StationAPI;

@Cancelable
@SuperBuilder
@EventPhases(StationAPI.INTERNAL_PHASE)
public class ServerLoginSuccessEvent extends Event {
    public final ClientPlayNetworkHandler networkHandler;
    public final LoginRequest0x1Packet loginRequestPacket;
}
