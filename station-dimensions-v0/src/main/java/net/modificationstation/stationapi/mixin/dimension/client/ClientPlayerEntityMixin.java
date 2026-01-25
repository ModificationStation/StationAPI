package net.modificationstation.stationapi.mixin.dimension.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.dimension.v1.registry.DimensionTypeRegistry;
import net.modificationstation.stationapi.api.entity.HasTeleportationManager;
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
        final var registry = DimensionTypeRegistry.INSTANCE;
        return world.dimension.hasWorldSpawn()
                ? dimensionId
                : registry.getLogicalId(registry.get(VanillaDimensions.OVERWORLD));
    }

    @Redirect(
            method = "tickMovement",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;changeDimension()V"
            )
    )
    private void stationapi_overrideSwitchDimensions(Minecraft minecraft) {
        //noinspection DataFlowIssue
        getTeleportationManager().switchDimension((ClientPlayerEntity) (Object) this);
    }
}
