package net.modificationstation.stationapi.mixin.celestial;

import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;
import net.modificationstation.stationapi.api.celestial.CelestialActivityStateManager;
import net.modificationstation.stationapi.api.celestial.WorldPropertiesWithWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldProperties.class)
public class WorldPropertiesMixin implements WorldPropertiesWithWorld {
    @Shadow private long time;

    @Unique
    private World world;

    @Inject(
            method = "setTime",
            at = @At("HEAD")
    )
    private void stationapi_celestialEventTimeManager(CallbackInfo ci) {
        if (((CelestialActivityStateManager) world).getCelestialTimeManager() == null) {
            return;
        }
        long daytime = time % 24000;
        long currentDay = time / 24000;
        if (daytime >= 0 && daytime < 6000) {
            ((CelestialActivityStateManager) world).getCelestialTimeManager().startMorningEvents(time, currentDay);
        } else if (daytime >= 6000 && daytime < 12000) {
            ((CelestialActivityStateManager) world).getCelestialTimeManager().startNoonEvents(time, currentDay);
        } else if (daytime >= 12000 && daytime < 18000) {
            ((CelestialActivityStateManager) world).getCelestialTimeManager().startEveningEvents(time, currentDay);
        } else if (daytime >= 18000) {
            ((CelestialActivityStateManager) world).getCelestialTimeManager().startMidnightEvents(time, currentDay);
        }
    }

    @Override
    public void setWorld(World world) {
        this.world = world;
    }

    @Override
    public World getWorld() {
        return world;
    }
}
