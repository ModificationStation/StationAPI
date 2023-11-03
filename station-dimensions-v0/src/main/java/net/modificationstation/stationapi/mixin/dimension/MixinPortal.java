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
public class MixinPortal implements TeleportationManager {

    @Inject(
            method = "onEntityCollision(Lnet/minecraft/level/Level;IIILnet/minecraft/entity/EntityBase;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/EntityBase;method_1388()V"
            )
    )
    private void onEntityCollision(World level, int x, int y, int z, Entity entityBase, CallbackInfo ci) {
        if (entityBase instanceof HasTeleportationManager manager)
            manager.setTeleportationManager(this);
    }
}
