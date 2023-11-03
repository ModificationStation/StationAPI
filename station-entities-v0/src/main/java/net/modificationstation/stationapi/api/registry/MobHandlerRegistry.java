package net.modificationstation.stationapi.api.registry;

import com.mojang.serialization.Lifecycle;
import java.util.function.Function;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

public final class MobHandlerRegistry extends SimpleRegistry<Function<World, LivingEntity>> {

    private static final Function<World, LivingEntity> EMPTY = level -> null;
    public static final RegistryKey<MobHandlerRegistry> KEY = RegistryKey.ofRegistry(MODID.id("mob_handlers"));
    public static final MobHandlerRegistry INSTANCE = Registries.create(KEY, new MobHandlerRegistry(), registry -> EMPTY, Lifecycle.experimental());

    private MobHandlerRegistry() {
        super(KEY, Lifecycle.experimental(), false);
    }
}
