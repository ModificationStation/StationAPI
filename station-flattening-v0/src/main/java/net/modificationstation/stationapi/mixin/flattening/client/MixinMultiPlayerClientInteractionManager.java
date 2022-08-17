package net.modificationstation.stationapi.mixin.flattening.client;

import net.minecraft.block.BlockBase;
import net.minecraft.client.BaseClientInteractionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MultiPlayerClientInteractionManager;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.util.maths.TilePos;
import net.modificationstation.stationapi.api.level.BlockStateView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MultiPlayerClientInteractionManager.class)
public class MixinMultiPlayerClientInteractionManager extends BaseClientInteractionManager {

    public MixinMultiPlayerClientInteractionManager(Minecraft minecraft) {
        super(minecraft);
    }

    @Redirect(
            method = {
                    "method_1707(IIII)V",
                    "method_1721(IIII)V"
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockBase;getHardness(Lnet/minecraft/entity/player/PlayerBase;)F"
            )
    )
    private float getHardnessPerMeta(BlockBase blockBase, PlayerBase arg, int i, int j, int k, int i1) {
        return ((BlockStateView) minecraft.level).getBlockState(i, j, k).calcBlockBreakingDelta(arg, minecraft.level, new TilePos(i, j, k));
    }
}
