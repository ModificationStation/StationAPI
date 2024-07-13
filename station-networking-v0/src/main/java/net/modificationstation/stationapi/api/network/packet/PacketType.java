package net.modificationstation.stationapi.api.network.packet;

import lombok.Setter;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.registry.PacketTypeRegistry;
import net.modificationstation.stationapi.api.registry.RegistryEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public final class PacketType<T extends Packet & ManagedPacket<T>> {
    @SuppressWarnings("unchecked")
    public final RegistryEntry.Reference<PacketType<T>> registryEntry = (RegistryEntry.Reference<PacketType<T>>) (RegistryEntry.Reference<?>) PacketTypeRegistry.INSTANCE.createEntry(this);
    public final boolean clientBound;
    public final boolean serverBound;
    public final ManagedPacket.@NotNull Factory<T> factory;
    @Setter
    private @Nullable NetworkHandler handler;

    public PacketType(
            boolean clientBound, boolean serverBound,
            @NotNull ManagedPacket.Factory<T> factory
    ) {
        this.clientBound = clientBound;
        this.serverBound = serverBound;
        this.factory = factory;
    }

    public Optional<NetworkHandler> getHandler() {
        return Optional.ofNullable(handler);
    }
}
