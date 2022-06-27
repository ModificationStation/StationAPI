package net.modificationstation.stationapi.api.client.event.network;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.network.ClientPlayNetworkHandler;
import net.minecraft.packet.login.LoginRequest0x1Packet;

@SuperBuilder
public class ServerLoginSuccessEvent extends Event {

    @Getter
    private final boolean cancelable = true;

    public final ClientPlayNetworkHandler networkHandler;
    public final LoginRequest0x1Packet loginRequestPacket;

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
