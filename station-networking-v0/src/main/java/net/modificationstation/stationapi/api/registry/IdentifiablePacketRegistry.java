package net.modificationstation.stationapi.api.registry;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.modificationstation.stationapi.api.network.packet.IdentifiablePacket;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class IdentifiablePacketRegistry {
    private static final IdentifiablePacket.Factory EMPTY = () -> null;
    public static final RegistryKey<Registry<IdentifiablePacket.Factory>> KEY = RegistryKey.ofRegistry(NAMESPACE.id("packets"));
    public static final Registry<IdentifiablePacket.Factory> INSTANCE = Registries.create(KEY, registry -> EMPTY);
}
