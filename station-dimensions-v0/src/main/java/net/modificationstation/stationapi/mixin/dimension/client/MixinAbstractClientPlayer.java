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
public abstract class MixinAbstractClientPlayer extends PlayerEntity implements HasTeleportationManager {

    public MixinAbstractClientPlayer(World arg) {
        super(arg);
    }

    @SuppressWarnings("DefaultAnnotationParam")
    @ModifyConstant(
            method = "respawn()V",
            constant = @Constant(intValue = 0)
    )
    private int getRespawnDimension(int constant) {
        return world.dimension.method_1766() ? dimensionId : DimensionRegistry.INSTANCE.getLegacyId(VanillaDimensions.OVERWORLD).orElseThrow(() -> new IllegalStateException("Couldn't find overworld dimension in the registry!"));
    }

    @Redirect(
            method = "updateDespawnCounter()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;switchDimension()V"
            )
    )
    private void overrideSwitchDimensions(Minecraft minecraft) {
        getTeleportationManager().switchDimension((ClientPlayerEntity) (Object) this);
    }
}
