package net.modificationstation.stationloader.api.common.entity;

import net.minecraft.entity.EntityBase;
import net.minecraft.level.Level;
import net.modificationstation.stationloader.api.common.StationLoader;
import net.modificationstation.stationloader.api.common.registry.Identifier;
import net.modificationstation.stationloader.api.common.registry.Registry;
import uk.co.benjiweber.expressions.functions.QuadFunction;

public final class EntityHandlerRegistry extends Registry<QuadFunction<Level, Double, Double, Double, EntityBase>> {

    private EntityHandlerRegistry(Identifier identifier) {
        super(identifier);
    }

    @Override
    public int getRegistrySize() {
        return Integer.MAX_VALUE;
    }

    public static final EntityHandlerRegistry INSTANCE = new EntityHandlerRegistry(Identifier.of(StationLoader.INSTANCE.getModID(), "entity_handlers"));
}
