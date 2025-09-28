package net.modificationstation.stationapi.api.registry;

import com.mojang.serialization.Lifecycle;
import net.minecraft.stat.Stat;
import net.modificationstation.stationapi.api.event.registry.RegistryAttribute;
import net.modificationstation.stationapi.api.event.registry.RegistryAttributeHolder;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

public class StatRegistry extends SimpleRegistry<Stat> {
    public static final RegistryKey<Registry<Stat>> KEY = RegistryKey.ofRegistry(NAMESPACE.id("stats"));
    public static final StatRegistry INSTANCE = Registries.create(KEY, new StatRegistry(), Lifecycle.experimental());

    private StatRegistry() {
        super(KEY, Lifecycle.experimental(), true);
        RegistryAttributeHolder.get(this).addAttribute(RegistryAttribute.SYNCED);
    }
}