package net.modificationstation.stationapi.mixin.flattening.client;

import net.minecraft.MultiplayerInteractionManager;
import net.minecraft.block.Block;
import net.minecraft.client.InteractionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MultiplayerInteractionManager.class)
class MultiplayerInteractionManagerMixin extends InteractionManager {
    private MultiplayerInteractionManagerMixin(Minecraft minecraft) {
        super(minecraft);
    }

    @Redirect(
            method = {
                    "method_1707(IIII)V",
                    "method_1721(IIII)V"
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Block;getHardness(Lnet/minecraft/entity/player/PlayerEntity;)F"
            )
    )
    private float stationapi_getHardnessPerMeta(Block blockBase, PlayerEntity arg, int i, int j, int k, int i1) {
        return minecraft.world.getBlockState(i, j, k).calcBlockBreakingDelta(arg, minecraft.world, new BlockPos(i, j, k));
    }
}
