package net.modificationstation.stationapi.mixin.dimension.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.dimension.v1.event.registry.DimensionTypeRegistryEvent;
import net.modificationstation.stationapi.api.dimension.v1.registry.DimensionTypeRegistry;
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
            method = "init",
            at = @At("TAIL")
    )
    private void stationapi_initDimensions(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(new DimensionTypeRegistryEvent());
    }

    @Redirect(
            method = "respawnPlayer(ZI)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;changeDimension()V"
            )
    )
    private void stationapi_switchDimension(Minecraft minecraft) {
        DimensionHelper.switchDimension(
                player,
                DimensionTypeRegistry.INSTANCE.getEntryByLogicalId(player.dimensionId)
                        .orElseThrow(
                                () -> new IllegalArgumentException("Unknown dimension: " + player.dimensionId + "!")
                        ).value(),
                1,
                null
        );
    }
}
