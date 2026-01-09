package net.modificationstation.stationapi.api.registry;

import com.mojang.serialization.Lifecycle;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.stat.Stat;
import net.modificationstation.stationapi.api.event.registry.RegistryAttribute;
import net.modificationstation.stationapi.api.event.registry.RegistryAttributeHolder;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

public class StatRegistry extends SimpleRegistry<Stat> {
    public static final RegistryKey<Registry<Stat>> KEY = RegistryKey.ofRegistry(NAMESPACE.id("stats"));
    public static final StatRegistry INSTANCE = Registries.create(KEY, new StatRegistry(), Lifecycle.experimental());

    public static final int ACHIEVEMENT_ID_SHIFT = 0x500000;
    public static final Int2IntFunction SHIFTED_ACHIEVEMENT_ID = id -> id - ACHIEVEMENT_ID_SHIFT;
    public static final int ACHIEVEMENT_AUTO_ID = SHIFTED_ACHIEVEMENT_ID.applyAsInt(AUTO_ID);

    private StatRegistry() {
        super(KEY, Lifecycle.experimental(), true);
        RegistryAttributeHolder.get(this).addAttribute(RegistryAttribute.SYNCED);
        nextId = 0x1040000;
    }
}