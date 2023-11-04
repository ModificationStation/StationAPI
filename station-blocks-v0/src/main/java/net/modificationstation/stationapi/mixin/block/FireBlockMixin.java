package net.modificationstation.stationapi.mixin.block;

import net.minecraft.block.FireBlock;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.block.FireBurnableRegisterEvent;
import net.modificationstation.stationapi.api.registry.tag.BlockTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(FireBlock.class)
abstract class FireBlockMixin {
    @Shadow protected abstract void method_1822(int i, int j, int k);

    @Inject(
            method = "init",
            at = @At("RETURN")
    )
    private void stationapi_postBurnableRegister(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(FireBurnableRegisterEvent.builder().addBurnable(this::method_1822).build());
    }

    @ModifyConstant(
            method = "onTick",
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
    private int stationapi_allowInfiniburnBlocks(int constant, World world, int x, int y, int z, Random random) {
        return world.getBlockState(x, y - 1, z).isIn(BlockTags.INFINIBURN) ? 1 : 0;
    }
}
