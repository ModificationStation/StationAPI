package net.modificationstation.stationapi.api.client.event.network;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.packet.misc.Disconnect0xFFPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuperBuilder
public class MultiplayerLogoutEvent extends Event {
    @NotNull public final Disconnect0xFFPacket packet;
    @Nullable public final String[] stacktrace;
    public final boolean dropped;
}
