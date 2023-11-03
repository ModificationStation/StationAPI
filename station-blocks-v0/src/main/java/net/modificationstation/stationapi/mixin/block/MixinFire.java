package net.modificationstation.stationapi.mixin.block;

import net.minecraft.block.FireBlock;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.block.FireBurnableRegisterEvent;
import net.modificationstation.stationapi.api.tag.BlockTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(FireBlock.class)
public abstract class MixinFire {

    @Shadow protected abstract void addBurnable(int i, int j, int k);

    @Inject(
            method = "init()V",
            at = @At("RETURN")
    )
    private void postBurnableRegister(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(FireBurnableRegisterEvent.builder().addBurnable(this::addBurnable).build());
    }

    @ModifyConstant(
            method = "onScheduledTick",
            constant = {
                    @Constant(
                            intValue = 0,
                            ordinal = 0
                    ),
                    @Constant(
                            intValue = 1,
                            ordinal = 1
                    )
            }
    )
    private int stationapi_allowInfiniburnBlocks(int constant, World arg, int i, int j, int k, Random random) {
        return arg.getBlockState(i, j - 1, k).isIn(BlockTags.INFINIBURN) ? 1 : 0;
    }
}
