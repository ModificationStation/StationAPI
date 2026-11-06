package net.modificationstation.stationapi.mixin.vanillafix;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSource;
import net.modificationstation.stationapi.api.vanillafix.world.StationChunkCacheWorld;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(World.class)
public abstract class WorldMixin implements StationChunkCacheWorld {
    @Unique
    private final ObjectArrayList<BlockEntity> removedBlockEntities = new ObjectArrayList<>();
    
    @Shadow
    protected ChunkSource chunkSource;

    @Shadow
    public List blockEntities;

    @WrapWithCondition(method = "tickEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/BlockEntity;tick()V"))
    public boolean test1(BlockEntity blockEntity) {
        return blockEntity.world != null;
    }

    @WrapOperation(method = "tickEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getChunk(II)Lnet/minecraft/world/chunk/Chunk;", ordinal = 2))
    public Chunk test2(World world, int chunkX, int chunkZ, Operation<Chunk> original) {
        if (!this.chunkSource.isChunkLoaded(chunkX, chunkZ)) {
            return null;
        }

        return original.call(world, chunkX, chunkZ);
    }

    @Inject(method = "tickEntities", at = @At(value = "FIELD", target = "Lnet/minecraft/world/World;processingDeferred:Z", ordinal = 1, opcode = Opcodes.PUTFIELD))
    public void test3(CallbackInfo ci) {
        if (!this.removedBlockEntities.isEmpty()) {
            //noinspection unchecked
            this.blockEntities.removeAll(this.removedBlockEntities);
            this.removedBlockEntities.clear();
        }
    }

    @WrapOperation(method = "tickEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getChunk(II)Lnet/minecraft/world/chunk/Chunk;", ordinal = 3))
    public Chunk test4(World world, int chunkX, int chunkZ, Operation<Chunk> original) {
        if (!this.chunkSource.isChunkLoaded(chunkX, chunkZ)) {
            return null;
        }

        return original.call(world, chunkX, chunkZ);
    }

    @Unique
    public void unloadBlockEntity(BlockEntity blockEntity) {
        this.removedBlockEntities.add(blockEntity);
    }
    
    @Inject(method = "setBlockEntity", at = @At(value = "HEAD"), cancellable = true)
    public void test5(int x, int y, int z, BlockEntity blockEntity, CallbackInfo ci) {
        if (blockEntity == null) {
            ci.cancel();
        }
    }
}
