package net.modificationstation.stationapi.api.registry;

import com.mojang.serialization.Lifecycle;
import net.minecraft.entity.EntityBase;
import net.minecraft.level.Level;
import uk.co.benjiweber.expressions.function.QuadFunction;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

public final class EntityHandlerRegistry extends SimpleRegistry<QuadFunction<Level, Double, Double, Double, EntityBase>> {

    private static final QuadFunction<Level, Double, Double, Double, EntityBase> EMPTY = (level, x, y, z) -> null;
    public static final RegistryKey<EntityHandlerRegistry> KEY = RegistryKey.ofRegistry(MODID.id("entity_handlers"));
    public static final EntityHandlerRegistry INSTANCE = Registries.create(KEY, new EntityHandlerRegistry(), registry -> EMPTY, Lifecycle.experimental());

    private EntityHandlerRegistry() {
        super(KEY, Lifecycle.experimental(), false);
    }
}
