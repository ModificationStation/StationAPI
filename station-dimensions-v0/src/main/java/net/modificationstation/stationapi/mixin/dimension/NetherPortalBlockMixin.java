package net.modificationstation.stationapi.mixin.dimension;

import net.minecraft.block.NetherPortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.entity.HasTeleportationManager;
import net.modificationstation.stationapi.api.world.dimension.TeleportationManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetherPortalBlock.class)
class NetherPortalBlockMixin implements TeleportationManager {
    @Inject(
            method = "onEntityCollision",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Entity;method_1388()V"
            )
    )
    private void stationapi_onEntityCollision(World world, int x, int y, int z, Entity entityBase, CallbackInfo ci) {
        if (entityBase instanceof HasTeleportationManager manager)
            manager.setTeleportationManager(this);
    }
}
