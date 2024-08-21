package net.modificationstation.stationapi.api.network.packet;

import lombok.Setter;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.registry.PacketTypeRegistry;
import net.modificationstation.stationapi.api.registry.RegistryEntry;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public final class PacketType<T extends Packet & ManagedPacket<T>> {
    public final RegistryEntry.Reference<PacketType<T>> registryEntry;
    public final boolean clientBound;
    public final boolean serverBound;
    public final ManagedPacket.@NotNull Factory<T> factory;
    @Setter
    private @Nullable NetworkHandler handler;
    public final boolean blocking;

    private PacketType(Builder<T> builder) {
        //noinspection unchecked
        registryEntry = (RegistryEntry.Reference<PacketType<T>>) (RegistryEntry.Reference<?>) PacketTypeRegistry.INSTANCE.createReservedEntry(builder.rawId, this);
        clientBound = builder.clientBound;
        serverBound = builder.serverBound;
        factory = builder.factory;
        blocking = builder.blocking;
    }

    public Optional<NetworkHandler> getHandler() {
        return Optional.ofNullable(handler);
    }

    public static <T extends Packet & ManagedPacket<T>> Builder<T> builder(
            boolean clientBound, boolean serverBound,
            @NotNull ManagedPacket.Factory<T> factory
    ) {
        return new Builder<>(clientBound, serverBound, factory);
    }

    public static final class Builder<T extends Packet & ManagedPacket<T>> {
        private int rawId = PacketTypeRegistry.AUTO_ID;
        private final boolean clientBound;
        private final boolean serverBound;
        private final ManagedPacket.Factory<T> factory;
        private boolean blocking;

        private Builder(
                boolean clientBound, boolean serverBound,
                @NotNull ManagedPacket.Factory<T> factory
        ) {
            this.clientBound = clientBound;
            this.serverBound = serverBound;
            this.factory = Objects.requireNonNull(factory, "Attempted to build a PacketType with null packet factory!");
        }

        @ApiStatus.Internal
        public Builder<T> rawId(int rawId) {
            this.rawId = rawId;
            return this;
        }

        public Builder<T> blocking() {
            this.blocking = true;
            return this;
        }

        public PacketType<T> build() {
            return new PacketType<>(this);
        }
    }
}
