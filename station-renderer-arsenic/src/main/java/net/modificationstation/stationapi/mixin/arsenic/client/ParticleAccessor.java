package net.modificationstation.stationapi.mixin.arsenic.client;

import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Particle.class)
public interface ParticleAccessor {
    @Accessor
    int getTextureId();

    @Accessor
    float getPrevU();

    @Accessor
    float getPrevV();

    @Accessor
    float getScale();

    @Accessor
    float getRed();

    @Accessor
    float getGreen();

    @Accessor
    float getBlue();
}
