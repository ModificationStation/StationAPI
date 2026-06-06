package net.modificationstation.stationapi.mixin.block;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.block.LeavesBlock;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.context.BlockTagContext;
import net.modificationstation.stationapi.api.registry.tag.BlockTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LeavesBlock.class)
public class LeavesBlockMixin {

    @WrapOperation(method = "onTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockId(III)I"))
    private int makeModdedLogsAndLeavesWork(World world, int x, int y, int z, Operation<Integer> original) {
        BlockState state = world.getBlockState(x, y, z);
        if (state.isIn(BlockTags.LOGS, BlockTagContext.of(world, x, y, z))) {
            return Block.LOG.id;
        }
        if (state.isIn(BlockTags.LEAVES, BlockTagContext.of(world, x, y, z))) {
            return Block.LEAVES.id;
        }

        return original.call(world, x, y, z);
    }
}
