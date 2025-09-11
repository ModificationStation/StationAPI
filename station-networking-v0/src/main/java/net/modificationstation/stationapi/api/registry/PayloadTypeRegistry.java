package net.modificationstation.stationapi.api.registry;

import com.mojang.serialization.Lifecycle;
import net.modificationstation.stationapi.api.event.registry.RegistryAttribute;
import net.modificationstation.stationapi.api.event.registry.RegistryAttributeHolder;
import net.modificationstation.stationapi.api.network.PacketByteBuf;
import net.modificationstation.stationapi.api.network.packet.Payload;
import net.modificationstation.stationapi.api.network.packet.PayloadType;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

public class PayloadTypeRegistry extends SimpleRegistry<PayloadType<? extends PacketByteBuf, ? extends Payload>> {
    public static final RegistryKey<Registry<PayloadType<? extends PacketByteBuf, ? extends Payload>>> KEY = RegistryKey.ofRegistry(NAMESPACE.id("payload_types"));
    public static final Registry<PayloadType<? extends PacketByteBuf, ? extends Payload>> INSTANCE = Registries.create(KEY, new PayloadTypeRegistry(), Lifecycle.experimental());

    private PayloadTypeRegistry() {
        super(KEY, Lifecycle.experimental(), false);
        RegistryAttributeHolder.get(this).addAttribute(RegistryAttribute.SYNCED);
    }
}
