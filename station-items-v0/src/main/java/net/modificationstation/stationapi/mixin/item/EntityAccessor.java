package net.modificationstation.stationapi.mixin.item;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Random;

@Mixin(Entity.class)
public interface EntityAccessor {
    @Accessor("random")
    Random stationapi_getRandom();
}
