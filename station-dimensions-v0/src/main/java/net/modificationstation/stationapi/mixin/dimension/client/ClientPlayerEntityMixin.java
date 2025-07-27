package net.modificationstation.stationapi.mixin.dimension.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.entity.HasTeleportationManager;
import net.modificationstation.stationapi.api.registry.DimensionRegistry;
import net.modificationstation.stationapi.api.world.dimension.VanillaDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientPlayerEntity.class)
abstract class ClientPlayerEntityMixin extends PlayerEntity implements HasTeleportationManager {
    private ClientPlayerEntityMixin(World arg) {
        super(arg);
    }

    @ModifyConstant(
            method = "respawn",
            constant = @Constant(intValue = 0)
    )
    private int stationapi_getRespawnDimension(int constant) {
        return world.dimension.hasWorldSpawn() ? dimensionId : DimensionRegistry.INSTANCE.getLegacyId(VanillaDimensions.OVERWORLD).orElseThrow(() -> new IllegalStateException("Couldn't find overworld dimension in the registry!"));
    }

    @Redirect(
            method = "method_937",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;method_2139()V"
            )
    )
    private void stationapi_overrideSwitchDimensions(Minecraft minecraft) {
        //noinspection DataFlowIssue
        getTeleportationManager().switchDimension((ClientPlayerEntity) (Object) this);
    }
}
