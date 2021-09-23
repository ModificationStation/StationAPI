package net.modificationstation.stationapi.api.client.event.network;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.network.ClientPlayNetworkHandler;
import net.minecraft.packet.login.LoginRequest0x1Packet;

@RequiredArgsConstructor
public class ServerLoginSuccessEvent extends Event {

    @Getter
    private final boolean cancellable = true;

    public final ClientPlayNetworkHandler networkHandler;
    public final LoginRequest0x1Packet loginRequestPacket;

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
