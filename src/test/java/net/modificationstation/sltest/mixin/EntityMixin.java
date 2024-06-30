package net.modificationstation.sltest.mixin;

import net.minecraft.entity.Entity;
import net.modificationstation.sltest.celestial.CelestialListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {

    @Shadow public float yaw;

    @Shadow public float pitch;

    @Shadow public double velocityY;

    @Shadow public double velocityX;

    @Shadow public double velocityZ;

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    public void spinEntity(CallbackInfo ci) {
        if (CelestialListener.spinningDimando.isActive()) {
            this.yaw = (this.yaw + 0.1F) % 360.0F;
            this.pitch = (this.pitch + 0.1F) % 360.0F;
        }
        if (CelestialListener.flyingDimando.isActive()) {
            if (this.velocityY < 0.5F) {
                this.velocityY += 0.05F;
            }
        }
        if (CelestialListener.fallingDimando.isActive()) {
            if (this.velocityY > -0.1F) {
                this.velocityY -= 0.05F;
            }
        }
        if (CelestialListener.longDimando.isActive()) {
            this.velocityX *= 1.1F;
            this.velocityZ *= 1.1F;
        }
    }
}
