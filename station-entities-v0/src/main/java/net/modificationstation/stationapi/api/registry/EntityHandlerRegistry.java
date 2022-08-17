package net.modificationstation.stationapi.api.registry;

import com.mojang.serialization.Lifecycle;
import net.minecraft.entity.EntityBase;
import net.minecraft.level.Level;
import uk.co.benjiweber.expressions.function.QuadFunction;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

public final class EntityHandlerRegistry extends SimpleRegistry<QuadFunction<Level, Double, Double, Double, EntityBase>> {

    public static final RegistryKey<Registry<QuadFunction<Level, Double, Double, Double, EntityBase>>> KEY = RegistryKey.ofRegistry(MODID.id("entity_handlers"));
    public static final EntityHandlerRegistry INSTANCE = Registry.create(KEY, new EntityHandlerRegistry(), Lifecycle.experimental());

    private EntityHandlerRegistry() {
        super(KEY, Lifecycle.experimental(), null);
    }
}
