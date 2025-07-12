package net.modificationstation.stationapi.api.effect;

import com.mojang.serialization.Lifecycle;
import net.modificationstation.stationapi.api.registry.Registries;
import net.modificationstation.stationapi.api.registry.RegistryKey;
import net.modificationstation.stationapi.api.registry.SimpleRegistry;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

public class EntityEffectTypeRegistry extends SimpleRegistry<EntityEffectType<?>> {
    public static final RegistryKey<EntityEffectTypeRegistry> KEY = RegistryKey.ofRegistry(NAMESPACE.id("entity_effects"));
    public static final EntityEffectTypeRegistry INSTANCE = Registries.create(KEY, new EntityEffectTypeRegistry(), registry -> null, Lifecycle.experimental());
    
    private EntityEffectTypeRegistry() {
        super(KEY, Lifecycle.experimental(), true);
    }
}
