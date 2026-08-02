package net.modificationstation.stationapi.mixin.vanillafix;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(Chunk.class)
public class ChunkMixin {
    @Shadow
    public Map blockEntities;

    @Inject(method = "setBlockEntity", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
    public void notifySet(int localX, int y, int localZ, BlockEntity blockEntity, CallbackInfo ci) {
        if (blockEntity instanceof ChestBlockEntity chest && chest.getStack(0) != null && chest.getStack(0).getItem() instanceof BucketItem) {
            System.err.println("SET");
        }
    }
    
    @Inject(method = "removeBlockEntityAt", at = @At("HEAD"))
    public void notifyRemove(int localX, int y, int localZ, CallbackInfo ci) {
        BlockPos var4 = new BlockPos(localX, y, localZ);

        BlockEntity blockEntity = (BlockEntity)this.blockEntities.remove(var4);
        if (blockEntity instanceof ChestBlockEntity chest && chest.getStack(0) != null && chest.getStack(0).getItem() instanceof BucketItem) {
            System.err.println("REMOVE");
        }
    }
    
//    @WrapOperation(method = "setBlockEntity", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
//    public <K extends BlockPos, V extends BlockEntity> V isItStillThere(Map instance, K k, V v, Operation<V> original) {
//        return original.call(instance, k, v);
//    }
}
