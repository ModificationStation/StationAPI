package net.modificationstation.stationapi.mixin.vanillafix;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.modificationstation.stationapi.api.vanillafix.world.StationChunkCacheWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Chunk.class)
public class ChunkMixin {
    @Shadow
    public World world;

    
    @WrapOperation(method = "unload", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/BlockEntity;markRemoved()V"))
    public void test(BlockEntity blockEntity, Operation<Void> original) {
        ((StationChunkCacheWorld)this.world).unloadBlockEntity(blockEntity);
    }
}
