package net.modificationstation.stationapi.mixin.render.client;

import net.minecraft.entity.ParticleBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ParticleBase.class)
public interface ParticleBaseAccessor {

    @Accessor
    int getField_2635();

    @Accessor
    float getField_2636();

    @Accessor
    float getField_2637();

    @Accessor
    float getField_2640();

    @Accessor
    float getField_2642();

    @Accessor
    float getField_2643();

    @Accessor
    float getField_2644();
}
