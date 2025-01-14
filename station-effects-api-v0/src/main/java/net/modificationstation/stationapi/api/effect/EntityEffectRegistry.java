package net.modificationstation.stationapi.api.effect;

import com.mojang.serialization.Lifecycle;
import net.modificationstation.stationapi.api.registry.Registries;
import net.modificationstation.stationapi.api.registry.RegistryKey;
import net.modificationstation.stationapi.api.registry.SimpleRegistry;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

public class EntityEffectRegistry extends SimpleRegistry<EntityEffectFactory> {
    public static final RegistryKey<EntityEffectRegistry> KEY = RegistryKey.ofRegistry(NAMESPACE.id("entity_effects"));
    public static final EntityEffectRegistry INSTANCE = Registries.create(KEY, new EntityEffectRegistry(), registry -> null, Lifecycle.experimental());
    
    private EntityEffectRegistry() {
        super(KEY, Lifecycle.experimental(), false);
    }
}
