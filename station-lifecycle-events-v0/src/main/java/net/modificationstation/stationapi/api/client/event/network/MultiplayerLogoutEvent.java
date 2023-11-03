package net.modificationstation.stationapi.api.client.event.network;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.network.packet.play.DisconnectPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuperBuilder
public class MultiplayerLogoutEvent extends Event {
    @NotNull public final DisconnectPacket packet;
    @Nullable public final String[] stacktrace;
    public final boolean dropped;
}
