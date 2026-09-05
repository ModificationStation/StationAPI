package net.modificationstation.stationapi.mixin.block;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.block.CropBlock;
import net.minecraft.block.PlantBlock;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.CustomFarmlandBlock;
import net.modificationstation.stationapi.api.block.context.BlockTagContext;
import net.modificationstation.stationapi.api.registry.tag.BlockTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CropBlock.class)
public class CropBlockMixin extends PlantBlockMixin {
    @Override
    public boolean injectCanPlantOnTopCanPlaceAt(PlantBlock plantBlock, int id, Operation<Boolean> original, World world, int x, int y, int z) {
        BlockState state = world.getBlockState(x,y - 1, z);
        if (state.isIn(BlockTags.FARMLANDS, BlockTagContext.of(world, x, y -1, z))) {
            return true;
        }
        
        return original.call(plantBlock, id);
    }

    @Override
    public boolean injectCanPlantOnTopCanGrow(PlantBlock plantBlock, int id, Operation<Boolean> original, World world, int x, int y, int z) {
        BlockState state = world.getBlockState(x,y - 1, z);
        if (state.isIn(BlockTags.FARMLANDS, BlockTagContext.of(world, x, y -1, z))) {
            return true;
        }

        return original.call(plantBlock, id);
    }
    
    
    @WrapOperation(method = "getAvailableMoisture", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockId(III)I", ordinal = 8))
    public int getCustomFarmlandMoisture(World world, int x, int y, int z, Operation<Integer> original) {
        int result = original.call(world, x, y, z);
        
        if (result != Block.FARMLAND.id) {
            BlockState state = world.getBlockState(x,y, z);
            if (state.isIn(BlockTags.FARMLANDS, BlockTagContext.of(world, x, y, z))) {
                return Block.FARMLAND.id;
            }
        }
        
        return result;
    }
    
    @WrapOperation(method = "getAvailableMoisture", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockMeta(III)I"))
    public int getCustomFarmlandWet(World world, int x, int y, int z, Operation<Integer> original) {
        Block block = Block.BLOCKS[world.getBlockId(x,y,z)];
        
        if (block != Block.FARMLAND && block instanceof CustomFarmlandBlock customFarmlandBlock) {
            return customFarmlandBlock.isWet(world, x, y, z) ? 7 : 0;
        }

        return original.call(world, x, y, z);
    }
}
