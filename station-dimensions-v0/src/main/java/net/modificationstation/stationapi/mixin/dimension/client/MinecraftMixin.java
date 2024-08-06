package net.modificationstation.stationapi.mixin.dimension.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.registry.DimensionRegistryEvent;
import net.modificationstation.stationapi.api.registry.DimensionRegistry;
import net.modificationstation.stationapi.api.world.dimension.DimensionHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
class MinecraftMixin {
    @Shadow public ClientPlayerEntity player;

    @Inject(
            method = "run",
            at = @At("HEAD")
    )
    private void stationapi_initDimensions(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(new DimensionRegistryEvent());
    }

    @Redirect(
            method = "method_2122(ZI)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;method_2139()V"
            )
    )
    private void stationapi_switchDimension(Minecraft minecraft) {
        DimensionHelper.switchDimension(player, DimensionRegistry.INSTANCE.getIdByLegacyId(player.dimensionId).orElseThrow(() -> new IllegalArgumentException("Unknown dimension: " + player.dimensionId + "!")), 1, null);
    }
}
