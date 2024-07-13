package net.modificationstation.stationapi.api.network.packet;

import net.minecraft.network.packet.Packet;
import org.jetbrains.annotations.NotNull;

public interface ManagedPacket<T extends Packet & ManagedPacket<T>> {
    int PACKET_ID = 254;

    @NotNull PacketType<T> getType();

    @FunctionalInterface
    interface Factory<T extends Packet & ManagedPacket<T>> {
        @NotNull T create();
    }
}
