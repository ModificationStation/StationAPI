package net.modificationstation.stationapi.api.common.entity;

import net.minecraft.entity.EntityBase;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.common.registry.Identifier;
import net.modificationstation.stationapi.api.common.registry.Registry;
import net.modificationstation.stationapi.impl.common.StationAPI;
import uk.co.benjiweber.expressions.functions.QuadFunction;

public final class EntityHandlerRegistry extends Registry<QuadFunction<Level, Double, Double, Double, EntityBase>> {

    public static final EntityHandlerRegistry INSTANCE = new EntityHandlerRegistry(Identifier.of(StationAPI.MODID, "entity_handlers"));

    private EntityHandlerRegistry(Identifier identifier) {
        super(identifier);
    }

    @Override
    public int getRegistrySize() {
        return Integer.MAX_VALUE;
    }
}
