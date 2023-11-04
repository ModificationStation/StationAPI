package net.modificationstation.stationapi.api.registry;

import com.mojang.serialization.Lifecycle;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import uk.co.benjiweber.expressions.function.QuadFunction;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

public final class EntityHandlerRegistry extends SimpleRegistry<QuadFunction<World, Double, Double, Double, Entity>> {

    private static final QuadFunction<World, Double, Double, Double, Entity> EMPTY = (level, x, y, z) -> null;
    public static final RegistryKey<EntityHandlerRegistry> KEY = RegistryKey.ofRegistry(NAMESPACE.id("entity_handlers"));
    public static final EntityHandlerRegistry INSTANCE = Registries.create(KEY, new EntityHandlerRegistry(), registry -> EMPTY, Lifecycle.experimental());

    private EntityHandlerRegistry() {
        super(KEY, Lifecycle.experimental(), false);
    }
}
