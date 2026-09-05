package net.modificationstation.stationapi.mixin.item;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.item.SeedsItem;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.context.BlockTagContext;
import net.modificationstation.stationapi.api.registry.tag.BlockTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SeedsItem.class)
class SeedsItemMixin {
    @WrapOperation(method = "useOnBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockId(III)I"))
    private int stationapi_allowPlacingOnFarmlandTag(World world, int x, int y, int z, Operation<Integer> original) {
        BlockState state = world.getBlockState(x,y, z);
        if (state.isIn(BlockTags.FARMLANDS, BlockTagContext.of(world, x, y, z))) {
            return Block.FARMLAND.id;
        }
        
        return original.call(world, x, y, z);
    }
}
