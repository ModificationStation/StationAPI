package net.modificationstation.stationapi.mixin.celestial;

import net.minecraft.world.PersistentState;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;
import net.modificationstation.stationapi.api.celestial.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(World.class)
abstract
class WorldMixin implements CelestialActivityStateManager {

    @Shadow public abstract PersistentState getOrCreateState(Class stateClass, String id);
    @Shadow public abstract void setState(String id, PersistentState state);

    @Shadow protected WorldProperties properties;
    @Unique
    private CelestialEventActivityState celestialEventActivityState;
    @Unique
    private CelestialTimeManager celestialTimeManager;

    @Inject(
            method = {
                    "<init>(Lnet/minecraft/world/dimension/DimensionData;Ljava/lang/String;Lnet/minecraft/world/dimension/Dimension;J)V",
                    "<init>(Lnet/minecraft/world/World;Lnet/minecraft/world/dimension/Dimension;)V",
                    "<init>(Lnet/minecraft/world/dimension/DimensionData;Ljava/lang/String;JLnet/minecraft/world/dimension/Dimension;)V"
            },
            at = @At("RETURN")
    )
    private void stationapi_onCor1(CallbackInfo ci) {
        ((WorldPropertiesWithWorld) properties).setWorld((World) (Object) this);
        celestialEventActivityState = (CelestialEventActivityState) getOrCreateState(CelestialEventActivityState.class, CelestialEventActivityState.ID);
        if (celestialEventActivityState == null) {
            celestialEventActivityState = new CelestialEventActivityState(CelestialEventActivityState.ID);
        }
        celestialTimeManager = new CelestialTimeManager((World) (Object) this);
        setState(CelestialEventActivityState.ID, celestialEventActivityState);
        CelestialEventRegistry.INSTANCE.initializeEvents((World) (Object) this);
    }

    @Override
    public CelestialEventActivityState getCelestialEvents() {
        return celestialEventActivityState;
    }

    @Override
    public CelestialTimeManager getCelestialTimeManager() {
        return celestialTimeManager;
    }
}
