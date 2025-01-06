package net.modificationstation.stationapi.api.event.registry;

import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.client.registry.MobHandlerRegistry;

import java.util.function.Function;

public class MobHandlerRegistryEvent extends RegistryEvent.EntryTypeBound<Function<World, LivingEntity>, MobHandlerRegistry> {
    public MobHandlerRegistryEvent() {
        super(MobHandlerRegistry.INSTANCE);
    }
}
