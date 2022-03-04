package net.modificationstation.stationapi.mixin.flattening;

import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockBase;
import net.minecraft.class_257;
import net.minecraft.level.Level;
import net.minecraft.level.LightType;
import net.minecraft.level.chunk.Chunk;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.BlockStateHolder;
import net.modificationstation.stationapi.api.block.States;
import net.modificationstation.stationapi.api.level.BlockStateView;
import net.modificationstation.stationapi.impl.level.StationLevelProperties;
import net.modificationstation.stationapi.impl.level.chunk.ChunkSection;
import net.modificationstation.stationapi.impl.level.chunk.ChunkSectionsAccessor;
import net.modificationstation.stationapi.impl.level.chunk.StationHeigtmapProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Chunk.class)
public abstract class MixinChunk implements ChunkSectionsAccessor, BlockStateView, StationHeigtmapProvider {
    @Shadow public Level level;
    @Shadow @Final public int x;
    @Shadow @Final public int z;

    @Shadow protected abstract void method_887(int i, int j);

    @Shadow public boolean field_967;
    @Shadow public byte[] tiles;
    @Shadow public int field_961;
    @Shadow public class_257 field_958;
    @Shadow public class_257 field_957;
    @Unique
    @Getter
    private ChunkSection[] sections;
    @Unique
    private short lastBlock;
    @Unique
    private final short[] stationHeightmap = new short[256];

//    @Inject(
//            method = "<init>(Lnet/minecraft/level/Level;[BII)V",
//            at = @At("RETURN")
//    )
//    private void hijackTiles(Level level, byte[] value, int chunkX, int chunkZ, CallbackInfo ci) {
//        for (int i = 0; i < value.length; i++) {
//            int id = Byte.toUnsignedInt(value[i]);
//            if (id == 0)
//                continue;
//            int x = (i >> 11) & 15;
//            int y = i & 127;
//            int z = (i >> 7) & 15;
//            int yOffset = y >> 4;
//            (sections[yOffset] == null ? sections[yOffset] = new ChunkSection(yOffset << 4) : sections[yOffset]).setBlockState(x, y & 15, z, ((BlockStateHolder) BlockBase.BY_ID[id]).getDefaultState(), false);
//            value[i] = 0;
//        }
//    }
    
    @Inject(method = "<init>(Lnet/minecraft/level/Level;II)V", at = @At("TAIL"))
    private void onChunkCreation(Level level, int x, int z, CallbackInfo info) {
        StationLevelProperties properties = StationLevelProperties.class.cast(level.getProperties());
        sections = new ChunkSection[properties.getSectionCount()];
        lastBlock = (short) (properties.getLevelHeight() - 1);
    }
    
    @Inject(method = "getHeight(II)I", at = @At("HEAD"), cancellable = true)
    private void getHeight(int x, int z, CallbackInfoReturnable<Integer> info) {
        info.setReturnValue((int) stationHeightmap[z << 4 | x]);
    }
    
    @Inject(method = "isAboveGround", at = @At("HEAD"), cancellable = true)
    private void isAboveGround(int localX, int y, int localZ, CallbackInfoReturnable<Boolean> info) {
        short height = stationHeightmap[localZ << 4 | localX];
        info.setReturnValue(y >= height);
    }
    
    @Override
    public byte[] getStoredHeightmap() {
        byte[] heightmap = new byte[512];
        for (short i = 0; i < 512; i++) {
            short value = stationHeightmap[i >> 1];
            byte val = (i & 1) == 0 ? (byte) (value & 255) : (byte) ((value >> 8) & 255);
            heightmap[i] = val;
        }
        return heightmap;
    }
    
    @Override
    public void loadStoredHeightmap(byte[] heightmap) {
        if (heightmap.length == 256) {
            for (short i = 0; i < 256; i++) {
                stationHeightmap[i] = (short) (heightmap[i] & 255);
            }
        }
        else {
            for (short i = 0; i < 256; i++) {
                int index = i << 1;
                stationHeightmap[i] = (short) (heightmap[index] | (heightmap[index | 1] << 8));
            }
        }
    }

    /**
     * @reason early version
     * @author mine_diver
     */
    @Overwrite
    @Environment(EnvType.CLIENT)
    public void method_892() {
        int chunkHeight = lastBlock;

        for(int x = 0; x < 16; ++x) {
            for(int z = 0; z < 16; ++z) {
                short height;
                for (height = lastBlock; height >0; height--) {
                    if (BlockBase.LIGHT_OPACITY[getTileId(x, height - 1, z)] != 0) break;
                }
                stationHeightmap[z << 4 | x] = height;
                if (height < chunkHeight) {
                    chunkHeight = height;
                }
            }
        }

        this.field_961 = chunkHeight;
        this.field_967 = true;
    }

    /**
     * @reason early version
     * @author mine_diver
     */
    @Overwrite
    public void generateHeightmap() {
        if (tiles != null) {
            for (int i = 0; i < tiles.length; i++) {
                int id = Byte.toUnsignedInt(tiles[i]);
                if (id == 0) continue;
                int x = (i >> 11) & 15;
                int y = i & 127;
                int z = (i >> 7) & 15;
                int yOffset = y >> 4;
                if (sections[yOffset] == ChunkSection.EMPTY_SECTION) {
                    sections[yOffset] = new ChunkSection(yOffset << 4);
                }
                else {
                    BlockStateHolder holder = BlockStateHolder.class.cast(BlockBase.BY_ID[id]);
                    sections[yOffset].setBlockState(x, y & 15, z, holder.getDefaultState(), false);
                }
                tiles[i] = 0;
            }
            tiles = null;
        }
        
        int minHeight = lastBlock;
        for(int x = 0; x < 16; ++x) {
            for(int z = 0; z < 16; ++z) {
                short height = lastBlock;

                while (height > 0) {
                    if (BlockBase.LIGHT_OPACITY[getTileId(x, height - 1, z)] != 0) break;
                    --height;
                }
    
                stationHeightmap[z << 4 | x] = height;
                if (height < minHeight) {
                    minHeight = height;
                }

                if (!this.level.dimension.halvesMapping) {
                    int light = 15;
                    int lightY = lastBlock;

                    do {
                        light -= BlockBase.LIGHT_OPACITY[getTileId(x, lightY, z)];
                        if (light > 0) {
                            this.field_958.method_1704(x, lightY, z, light);
                        }

                        --lightY;
                    } while (lightY > 0 && light > 0);
                }
            }
        }

        this.field_961 = minHeight;

        for(int x = 0; x < 16; ++x) {
            for(int z = 0; z < 16; ++z) {
                this.method_887(x, z);
            }
        }

        this.field_967 = true;
    }

    /**
     * @reason early version
     * @author mine_diver
     */
    @Overwrite
    private void method_889(int x, int y, int z) {
        short height = stationHeightmap[z << 4 | x];
        short maxHeight = (short) Math.max(y, height);

        while (maxHeight > 0 && BlockBase.LIGHT_OPACITY[getTileId(x, maxHeight - 1, z)] == 0) {
            --maxHeight;
        }

        if (maxHeight != height) {
            this.level.method_240(x, z, maxHeight, height);
            stationHeightmap[z << 4 | x] = maxHeight;
            if (maxHeight < this.field_961) {
                this.field_961 = maxHeight;
            } else {
                int var7 = lastBlock;

                for(int var8 = 0; var8 < 16; ++var8) {
                    for(int var9 = 0; var9 < 16; ++var9) {
                        if ((stationHeightmap[(var9 << 4) | var8]) < var7) {
                            var7 = stationHeightmap[(var9 << 4) | var8];
                        }
                    }
                }

                this.field_961 = var7;
            }

            int var12 = this.x << 4 | x;
            int var13 = this.z << 4 | z;
            if (maxHeight < height) {
                for(int var14 = maxHeight; var14 < height; ++var14) {
                    this.field_958.method_1704(x, var14, z, 15);
                }
            } else {
                this.level.method_166(LightType.SKY, var12, height, var13, var12, maxHeight, var13);

                for(int var15 = height; var15 < maxHeight; ++var15) {
                    this.field_958.method_1704(x, var15, z, 0);
                }
            }

            int var16 = 15;

            int var10;
            for(var10 = maxHeight; maxHeight > 0 && var16 > 0; this.field_958.method_1704(x, maxHeight, z, var16)) {
                --maxHeight;
                int var11 = BlockBase.LIGHT_OPACITY[this.getTileId(x, maxHeight, z)];
                if (var11 == 0) {
                    var11 = 1;
                }

                var16 -= var11;
                if (var16 < 0) {
                    var16 = 0;
                }
            }

            while(maxHeight > 0 && BlockBase.LIGHT_OPACITY[this.getTileId(x, maxHeight - 1, z)] == 0) {
                --maxHeight;
            }

            if (maxHeight != var10) {
                this.level.method_166(LightType.SKY, var12 - 1, maxHeight, var13 - 1, var12 + 1, var10, var13 + 1);
            }

            this.field_967 = true;
        }
    }

    /**
     * @reason early version
     * @author mine_diver
     */
    @Overwrite
    public int getTileId(int x, int y, int z) {
        return getBlockState(x, y, z).getBlock().id;
    }

    /**
     * @reason early version
     * @author mine_diver
     */
    @Overwrite
    public boolean setTileWithMetadata(int x, int y, int z, int blockId, int meta) {
        BlockState state = ((BlockStateHolder) BlockBase.BY_ID[blockId]).getDefaultState();
        int yOffset = y >> 4;
        ChunkSection section = sections[yOffset];
        boolean sameMeta = this.field_957.method_1703(x, y, z) == meta;
        if (section == ChunkSection.EMPTY_SECTION) {
            if (state.isAir() && sameMeta)
                return false;
            section = new ChunkSection(yOffset << 4);
            this.sections[yOffset] = section;
        }
        short var6 = stationHeightmap[z << 4 | x];
        BlockState oldState = section.getBlockState(x, y & 15, z);
        if (oldState == state && sameMeta)
            return false;
        else {
            int levelX = this.x << 4 | x;
            int levelZ = this.z << 4 | z;
            section.setBlockState(x, y & 15, z, state);
            oldState.getBlock().onBlockRemoved(this.level, levelX, y, levelZ);
            this.field_957.method_1704(x, y, z, meta);

            if (!this.level.dimension.halvesMapping) {
                if (BlockBase.LIGHT_OPACITY[state.getBlock().id] != 0) {
                    if (y >= var6)
                        this.method_889(x, y + 1, z);
                } else if (y == var6 - 1)
                    this.method_889(x, y, z);

                this.level.method_166(LightType.SKY, levelX, y, levelZ, levelX, y, levelZ);
            }

            this.level.method_166(LightType.BLOCK, levelX, y, levelZ, levelX, y, levelZ);
            this.method_887(x, z);
            this.field_957.method_1704(x, y, z, meta);
            state.getBlock().onBlockPlaced(this.level, levelX, y, levelZ);

            this.field_967 = true;
            return true;
        }
    }

    /**
     * @reason early version
     * @author mine_diver
     */
    @Overwrite
    public boolean method_860(int x, int y, int z, int blockId) {
        return setBlockState(x, y, z, ((BlockStateHolder) BlockBase.BY_ID[blockId]).getDefaultState()) != null;
    }

    @Override
    @Unique
    public BlockState getBlockState(int x, int y, int z) {
        int yOffset = y >> 4;
        return sections[yOffset] == null ? States.AIR.get() : sections[yOffset].getBlockState(x, y & 15, z);
    }

    @Override
    @Unique
    public BlockState setBlockState(int x, int y, int z, BlockState state) {
        int yOffset = y >> 4;
        ChunkSection section = sections[yOffset];
        if (section == ChunkSection.EMPTY_SECTION) {
            if (state.isAir())
                return null;
            section = new ChunkSection(yOffset << 4);
            this.sections[yOffset] = section;
        }
        short topY = stationHeightmap[z << 4 | x];
        BlockState oldState = section.getBlockState(x, y & 15, z);
        if (oldState == state)
            return null;
        else {
            int levelX = this.x << 4 | x;
            int levelZ = this.z << 4 | z;
            oldState.getBlock().onBlockRemoved(this.level, levelX, y, levelZ);
            // moving this to after onBlockRemoved because some blocks may want to get their blockstate before being removed
            section.setBlockState(x, y & 15, z, state);

            this.field_957.method_1704(x, y, z, 0);
            if (!this.level.dimension.halvesMapping) {
                if (BlockBase.LIGHT_OPACITY[state.getBlock().id] != 0) {
                    if (y >= topY)
                        this.method_889(x, y + 1, z);
                } else if (y == topY - 1)
                    this.method_889(x, y, z);
                this.level.method_166(LightType.SKY, levelX, y, levelZ, levelX, y, levelZ);
            }

            this.level.method_166(LightType.BLOCK, levelX, y, levelZ, levelX, y, levelZ);
            this.method_887(x, z);
            if (!this.level.isClient) {
                state.getBlock().onBlockPlaced(this.level, levelX, y, levelZ);
            }

            this.field_967 = true;
            return oldState;
        }
    }

    @Redirect(
            method = "method_890()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/class_490;method_1671([B)V"
            )
    )
    private void stopRemoval(byte[] bs) {}
}
