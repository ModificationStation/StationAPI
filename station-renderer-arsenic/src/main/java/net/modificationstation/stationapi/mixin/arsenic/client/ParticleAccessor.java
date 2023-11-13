package net.modificationstation.stationapi.mixin.arsenic.client;

import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Particle.class)
public interface ParticleAccessor {
    @Accessor
    int getField_2635();

    @Accessor
    float getField_2636();

    @Accessor
    float getField_2637();

    @Accessor
    float getScale();

    @Accessor
    float getRed();

    @Accessor
    float getGreen();

    @Accessor
    float getBlue();
}
