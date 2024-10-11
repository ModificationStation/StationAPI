package net.modificationstation.stationapi.mixin.world;

import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.world.WorldEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(World.class)
abstract
class WorldMixin {
    @Shadow public PersistentStateManager persistentStateManager;

    @Shadow public abstract PersistentState getOrCreateState(Class stateClass, String id);

    @Shadow public abstract void setState(String id, PersistentState state);

    @Inject(
            method = {
                    "<init>(Lnet/minecraft/world/dimension/DimensionData;Ljava/lang/String;Lnet/minecraft/world/dimension/Dimension;J)V",
                    "<init>(Lnet/minecraft/world/World;Lnet/minecraft/world/dimension/Dimension;)V",
                    "<init>(Lnet/minecraft/world/dimension/DimensionData;Ljava/lang/String;JLnet/minecraft/world/dimension/Dimension;)V"
            },
            at = @At("RETURN")
    )
    private void stationapi_onCor1(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(WorldEvent.Init.builder().world(World.class.cast(this)).build());
    }
	
	@Inject(
            method = "method_195",
            at = @At("HEAD")
    )
	private void stationapi_onLevelSave(CallbackInfo ci) {
		StationAPI.EVENT_BUS.post(WorldEvent.Save.builder().world(World.class.cast(this)).build());
	}
}
