package net.modificationstation.stationapi.api.client.event.network;

import lombok.RequiredArgsConstructor;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.packet.misc.Disconnect0xFFPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
public class MultiplayerLogoutEvent extends Event {

    @NotNull public final Disconnect0xFFPacket packet;
    @Nullable public final String[] stacktrace;
    public final boolean dropped;

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
