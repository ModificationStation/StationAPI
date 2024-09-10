package net.modificationstation.stationapi.api.client.registry;

import com.mojang.serialization.Lifecycle;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.registry.Registries;
import net.modificationstation.stationapi.api.registry.RegistryKey;
import net.modificationstation.stationapi.api.registry.SimpleRegistry;

import java.util.function.Function;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

public final class MobHandlerRegistry extends SimpleRegistry<Function<World, LivingEntity>> {

    private static final Function<World, LivingEntity> EMPTY = world -> null;
    public static final RegistryKey<MobHandlerRegistry> KEY = RegistryKey.ofRegistry(NAMESPACE.id("mob_handlers"));
    public static final MobHandlerRegistry INSTANCE = Registries.create(KEY, new MobHandlerRegistry(), registry -> EMPTY, Lifecycle.experimental());

    private MobHandlerRegistry() {
        super(KEY, Lifecycle.experimental(), false);
    }
}
