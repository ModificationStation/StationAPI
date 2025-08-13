package net.modificationstation.stationapi.mixin.render;

import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.client.world.LightmapGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(World.class)
public abstract class WorldMixin implements LightmapGetter {

    // getTimeOfDay
    @Shadow
    public abstract float method_198(float f);

    // getRain
    @Shadow
    public abstract float method_221(float f);

    // getThunder
    @Shadow
    public abstract float method_213(float f);

    @Override
    public float getSkyBrightness(float tickDelta) {
        float timeOfDay = this.method_198(tickDelta);
        float brightness = 1.0F - (MathHelper.cos(timeOfDay * (float) Math.PI * 2.0F) * 2.0F + 0.2F);
        if (brightness < 0.0F) {
            brightness = 0.0F;
        }

        if (brightness > 1.0F) {
            brightness = 1.0F;
        }

        brightness = 1.0F - brightness;
        brightness = (float) (brightness * (1.0 - this.method_221(tickDelta) * 5.0F / 16.0));
        brightness = (float) (brightness * (1.0 - this.method_213(tickDelta) * 5.0F / 16.0));
        return brightness * 0.8F + 0.2F;
    }
}
