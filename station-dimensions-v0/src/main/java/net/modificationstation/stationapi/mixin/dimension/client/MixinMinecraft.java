package net.modificationstation.stationapi.mixin.dimension.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.AbstractClientPlayer;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.registry.DimensionRegistryEvent;
import net.modificationstation.stationapi.api.level.dimension.DimensionHelper;
import net.modificationstation.stationapi.api.registry.DimensionRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {

    @Shadow public AbstractClientPlayer player;

    @Inject(
            method = "init()V",
            at = @At("RETURN")
    )
    private void initDimensions(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(new DimensionRegistryEvent());
    }

    @Redirect(
            method = "method_2122(ZI)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;switchDimension()V"
            )
    )
    private void switchDimension(Minecraft minecraft) {
        DimensionHelper.switchDimension(player, DimensionRegistry.INSTANCE.getIdByLegacyId(player.dimensionId).orElseThrow(() -> new IllegalArgumentException("Unknown dimension: " + player.dimensionId + "!")), 1, null);
    }
}
