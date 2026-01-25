package net.modificationstation.stationapi.api.dimension.v1.registry;

import com.mojang.serialization.Lifecycle;
import net.modificationstation.stationapi.api.dimension.v1.DimensionType;
import net.modificationstation.stationapi.api.event.registry.RegistryAttribute;
import net.modificationstation.stationapi.api.event.registry.RegistryAttributeHolder;
import net.modificationstation.stationapi.api.registry.Registries;
import net.modificationstation.stationapi.api.registry.RegistryKey;
import net.modificationstation.stationapi.api.registry.SimpleLogicalRegistry;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

public class DimensionTypeRegistry extends SimpleLogicalRegistry<DimensionType<?>> {
    public static final RegistryKey<DimensionTypeRegistry> KEY = RegistryKey.ofRegistry(NAMESPACE.id("dimension_types"));
    public static final DimensionTypeRegistry INSTANCE = Registries.create(KEY, new DimensionTypeRegistry(), Lifecycle.experimental());

    private DimensionTypeRegistry() {
        super(KEY, Lifecycle.experimental(), true);
        RegistryAttributeHolder.get(this).addAttribute(RegistryAttribute.SYNCED);
    }
}
