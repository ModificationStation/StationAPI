package net.modificationstation.stationapi.api.registry;

import com.mojang.serialization.Lifecycle;
import net.minecraft.entity.Living;
import net.minecraft.level.Level;

import java.util.function.Function;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

public final class MobHandlerRegistry extends SimpleRegistry<Function<Level, Living>> {

    public static final RegistryKey<Registry<Function<Level, Living>>> KEY = RegistryKey.ofRegistry(MODID.id("mob_handlers"));
    public static final MobHandlerRegistry INSTANCE = Registry.create(KEY, new MobHandlerRegistry(), Lifecycle.experimental());

    private MobHandlerRegistry() {
        super(KEY, Lifecycle.experimental(), null);
    }
}
