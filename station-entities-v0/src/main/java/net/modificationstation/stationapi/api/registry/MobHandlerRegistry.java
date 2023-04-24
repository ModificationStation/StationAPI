package net.modificationstation.stationapi.api.registry;

import com.mojang.serialization.Lifecycle;
import net.minecraft.entity.Living;
import net.minecraft.level.Level;

import java.util.function.Function;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

public final class MobHandlerRegistry extends SimpleRegistry<Function<Level, Living>> {

    private static final Function<Level, Living> EMPTY = level -> null;
    public static final RegistryKey<MobHandlerRegistry> KEY = RegistryKey.ofRegistry(MODID.id("mob_handlers"));
    public static final MobHandlerRegistry INSTANCE = Registries.create(KEY, new MobHandlerRegistry(), registry -> EMPTY, Lifecycle.experimental());

    private MobHandlerRegistry() {
        super(KEY, Lifecycle.experimental(), false);
    }
}
