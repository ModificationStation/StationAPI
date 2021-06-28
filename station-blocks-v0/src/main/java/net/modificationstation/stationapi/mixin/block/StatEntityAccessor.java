package net.modificationstation.stationapi.mixin.block;

import net.minecraft.client.StatEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(StatEntity.class)
public interface StatEntityAccessor {

    @Accessor
    void setValue(int value);
}
