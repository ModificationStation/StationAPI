package net.modificationstation.stationapi.impl.packet;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.network.PacketHandler;
import net.modificationstation.stationapi.api.registry.Identifier;

import java.util.Map;
import java.util.Set;

public class IdentifiablePacketImpl {

    public static final Set<Identifier>
            SERVER_TO_CLIENT_PACKETS = new ReferenceOpenHashSet<>(),
            CLIENT_TO_SERVER_PACKETS = new ReferenceOpenHashSet<>();

    public static final Map<Identifier, PacketHandler> HANDLERS = new Reference2ReferenceOpenHashMap<>();
}
