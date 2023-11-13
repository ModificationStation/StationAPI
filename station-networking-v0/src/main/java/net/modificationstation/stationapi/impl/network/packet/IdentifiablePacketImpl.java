package net.modificationstation.stationapi.impl.network.packet;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.network.NetworkHandler;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Map;
import java.util.Set;

public class IdentifiablePacketImpl {
    public static final Set<Identifier>
            CLIENT_BOUND_PACKETS = new ReferenceOpenHashSet<>(),
            SERVER_BOUND_PACKETS = new ReferenceOpenHashSet<>();

    public static final Map<Identifier, NetworkHandler> HANDLERS = new Reference2ReferenceOpenHashMap<>();
}
