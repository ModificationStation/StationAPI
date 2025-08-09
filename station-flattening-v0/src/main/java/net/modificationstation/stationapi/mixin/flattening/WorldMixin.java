package net.modificationstation.stationapi.mixin.flattening;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.Dimension;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.States;
import net.modificationstation.stationapi.api.world.StationFlatteningWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(World.class)
abstract class WorldMixin implements StationFlatteningWorld {
    @Shadow public abstract Chunk getChunkFromPos(int x, int z);

    @Shadow @Final public Dimension dimension;

    @Shadow protected abstract void blockUpdate(int i, int j, int k, int l);
    
    @Shadow public abstract int getBlockId(int x, int y, int z);
    
    @Override
    @Unique
    public BlockState getBlockState(int x, int y, int z) {
        return getChunkFromPos(x, z).getBlockState(x & 15, y, z & 15);
    }

    @Override
    @Unique
    public BlockState setBlockState(int x, int y, int z, BlockState blockState) {
        return getChunkFromPos(x, z).setBlockState(x & 15, y, z & 15, blockState);
    }

    @Override
    @Unique
    public BlockState setBlockStateWithNotify(int x, int y, int z, BlockState blockState) {
        BlockState oldBlockState = setBlockState(x, y, z, blockState);
        if (oldBlockState != null) {
            blockUpdate(x, y, z, blockState.getBlock().id);
            return oldBlockState;
        }
        return null;
    }

    @Override
    @Unique
    public BlockState setBlockStateWithMetadata(int x, int y, int z, BlockState blockState, int meta) {
        return getChunkFromPos(x, z).setBlockStateWithMetadata(x & 0xF, y, z & 0xF, blockState, meta);
    }

    @Override
    @Unique
    public BlockState setBlockStateWithMetadataWithNotify(int x, int y, int z, BlockState blockState, int meta) {
        BlockState oldBlockState = setBlockStateWithMetadata(x, y, z, blockState, meta);
        if (oldBlockState != null) {
            blockUpdate(x, y, z, blockState.getBlock().id);
            return oldBlockState;
        }
        return null;
    }

    @ModifyVariable(
            method = "manageChunkUpdatesAndEvents",
            at = @At(
                    value = "STORE",
                    ordinal = 0
            ),
            index = 9
    )
    private int stationapi_changeCaveSoundY(int y) {
        return y + getBottomY();
    }

    @ModifyVariable(
            method = "manageChunkUpdatesAndEvents",
            at = @At(
                    value = "STORE",
                    ordinal = 2
            ),
            index = 10
    )
    private int stationapi_changeTickY(int y) {
        return y + getBottomY();
    }

    @Redirect(
            method = "manageChunkUpdatesAndEvents()V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/chunk/Chunk;blocks:[B",
                    args = "array=get"
            )
    )
    private byte stationapi_redirectTilesAccess(byte[] array, int index) {
        return 0;
    }

    @ModifyVariable(
            method = "manageChunkUpdatesAndEvents",
            at = @At(
                    value = "STORE",
                    ordinal = 1
            ),
            index = 11
    )
    private int stationapi_changeTickBlockId(
            int blockId,
            @Local(index = 5) Chunk chunk,
            @Local(index = 8) int x, @Local(index = 10) int y, @Local(index = 9) int z
    ) {
        return chunk.getBlockState(x, y, z).getBlock().id;
    }

    @ModifyConstant(method = {
        "getBlockId",
        "isPosLoaded",
        "isRegionLoaded(IIIIII)Z",
        "setBlockWithoutNotifyingNeighbors(IIII)Z",
        "setBlockWithoutNotifyingNeighbors(IIIII)Z",
        "getBlockMeta",
        "setBlockMetaWithoutNotifyingNeighbors",
        "isTopY",
        "getBrightness(III)I",
        "getLightLevel(IIIZ)I",
        "getBrightness(Lnet/minecraft/world/LightType;III)I",
        "setLight",
        "updateEntity(Lnet/minecraft/entity/Entity;Z)V",
        "manageChunkUpdatesAndEvents"
    }, constant = @Constant(intValue = 128))
    private int stationapi_changeMaxHeight(int value) {
        return getTopY();
    }

    @ModifyConstant(method = {
            "getBlockId",
            "setBlockWithoutNotifyingNeighbors(IIII)Z",
            "setBlockWithoutNotifyingNeighbors(IIIII)Z",
            "getBlockMeta",
            "setBlockMetaWithoutNotifyingNeighbors",
            "isTopY",
            "getBrightness(III)I",
            "getLightLevel(IIIZ)I",
            "getBrightness(Lnet/minecraft/world/LightType;III)I",
            "setLight"
    }, constant = @Constant(expandZeroConditions = Constant.Condition.GREATER_THAN_OR_EQUAL_TO_ZERO))
    private int stationapi_changeBottomYGE(int value) {
        return getBottomY();
    }

    @ModifyConstant(method = {
            "isPosLoaded",
            "isRegionLoaded(IIIIII)Z",
            "manageChunkUpdatesAndEvents",
            "raycast(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;ZZ)Lnet/minecraft/util/hit/HitResult;"
    }, constant = @Constant(expandZeroConditions = Constant.Condition.LESS_THAN_ZERO))
    private int stationapi_changeBottomYLT(int value) {
        return getBottomY();
    }

    @ModifyConstant(method = {
            "getTopSolidBlockY"
    }, constant = @Constant(expandZeroConditions = Constant.Condition.LESS_THAN_OR_EQUAL_TO_ZERO))
    private int stationapi_changeBottomYLE(int value) {
        return getBottomY();
    }

    @ModifyConstant(method = {
            "updateEntity(Lnet/minecraft/entity/Entity;Z)V",
            "getBrightness(Lnet/minecraft/world/LightType;III)I"
    }, constant = @Constant(intValue = 0, ordinal = 0))
    private int stationapi_changeBottomYFirst(int value) {
        return getBottomY();
    }

    @ModifyConstant(method = {
            "getTopY"
    }, constant = @Constant(intValue = 0))
    private int stationapi_changeBottomY(int constant) {
        return getBottomY();
    }

    @ModifyConstant(
            method = {
                    "getBrightness(III)I",
                    "getLightLevel(IIIZ)I",
                    "getBrightness(Lnet/minecraft/world/LightType;III)I",
                    "getTopSolidBlockY",
                    "manageChunkUpdatesAndEvents"
            },
            constant = @Constant(intValue = 127)
    )
    private int stationapi_changeMaxBlockHeight(int value) {
        return getTopY() - 1;
    }

    @ModifyConstant(method = {
        "raycast(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;ZZ)Lnet/minecraft/util/hit/HitResult;"
    }, constant = @Constant(intValue = 200))
    private int stationapi_changeMaxEntityCalcHeight(int value) {
        return getTopY() + 64;
    }

    @Unique
    @Override
    public int getHeight() {
        return dimension.getHeight();
    }

    @Override
    public int getBottomY() {
        return dimension.getBottomY();
    }

    @ModifyVariable(
            method = "canPlace",
            index = 8,
            at = @At(
                    value = "LOAD",
                    ordinal = 6
            )
    )
    private Block stationapi_accountForAirBlock(Block value) {
        return value == States.AIR.get().getBlock() ? null : value;
    }

    @ModifyVariable(
            method = "updateLight",
            at = @At(
                    value = "STORE",
                    ordinal = 1
            ),
            index = 5,
            argsOnly = true
    )
    private int stationapi_getStateLuminance(int original, @Local(index = 2) int x, @Local(index = 3) int y, @Local(index = 4) int z, @Local(index = 5) int light) {
        return Math.max(getBlockState(x, y, z).getLuminance(), light);
    }
    
    /*@Inject(method = "getSpawnBlockId", at = @At("HEAD"), cancellable = true)
    private void fixHeightSearch(int x, int z, CallbackInfoReturnable<Integer> info) {
        int top = getTopY() - 1;
        int id = this.getBlockId(x, 63, z);
        for (int i = 63; i < top; i++) {
            int nextId = this.getBlockId(x, i + 1, z);
            if (nextId == 0) {
                info.setReturnValue(id);
                return;
            }
            id = nextId;
        }
        info.setReturnValue(id);
    }*/
}
