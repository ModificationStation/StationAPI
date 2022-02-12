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
import net.modificationstation.stationapi.impl.level.chunk.ChunkSection;
import net.modificationstation.stationapi.impl.level.chunk.ChunkSectionsAccessor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Chunk.class)
public abstract class MixinChunk implements ChunkSectionsAccessor, BlockStateView {

    @Shadow public byte[] heightmap;
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
    private final ChunkSection[] sections = new ChunkSection[8];

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

    /**
     * @reason early version
     * @author mine_diver
     */
    @Overwrite
    @Environment(EnvType.CLIENT)
    public void method_892() {
        int var1 = 127;

        for(int var2 = 0; var2 < 16; ++var2) {
            for(int var3 = 0; var3 < 16; ++var3) {
                int var4 = 127;

                while (var4 > 0) {
                    if (BlockBase.LIGHT_OPACITY[getTileId(var2, var4 - 1, var3)] != 0)
                        break;
                    --var4;
                }

                this.heightmap[(var3 << 4) | var2] = (byte)var4;
                if (var4 < var1) {
                    var1 = var4;
                }
            }
        }

        this.field_961 = var1;
        this.field_967 = true;
    }

    /**
     * @reason early version
     * @author mine_diver
     */
    @Overwrite
    public void generateHeightmap() {
        for (int i = 0; i < tiles.length; i++) {
            int id = Byte.toUnsignedInt(tiles[i]);
            if (id == 0)
                continue;
            int x = (i >> 11) & 15;
            int y = i & 127;
            int z = (i >> 7) & 15;
            int yOffset = y >> 4;
            (sections[yOffset] == null ? sections[yOffset] = new ChunkSection(yOffset << 4) : sections[yOffset]).setBlockState(x, y & 15, z, ((BlockStateHolder) BlockBase.BY_ID[id]).getDefaultState(), false);
            tiles[i] = 0;
        }
        tiles = null;
        int var1 = 127;

        for(int var2 = 0; var2 < 16; ++var2) {
            for(int var3 = 0; var3 < 16; ++var3) {
                int var4 = 127;

                while (var4 > 0) {
                    if (BlockBase.LIGHT_OPACITY[getTileId(var2, var4 - 1, var3)] != 0) break;
                    --var4;
                }

                this.heightmap[(var3 << 4) | var2] = (byte)var4;
                if (var4 < var1) {
                    var1 = var4;
                }

                if (!this.level.dimension.halvesMapping) {
                    int var6 = 15;
                    int var7 = 127;

                    do {
                        var6 -= BlockBase.LIGHT_OPACITY[getTileId(var2, var7, var3)];
                        if (var6 > 0) {
                            this.field_958.method_1704(var2, var7, var3, var6);
                        }

                        --var7;
                    } while (var7 > 0 && var6 > 0);
                }
            }
        }

        this.field_961 = var1;

        for(int var8 = 0; var8 < 16; ++var8) {
            for(int var9 = 0; var9 < 16; ++var9) {
                this.method_887(var8, var9);
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
        int var4 = this.heightmap[(z << 4) | x] & 255;
        int var5 = Math.max(y, var4);

        while (var5 > 0 && BlockBase.LIGHT_OPACITY[getTileId(x, var5 - 1, z)] == 0) {
            --var5;
        }

        if (var5 != var4) {
            this.level.method_240(x, z, var5, var4);
            this.heightmap[(z << 4) | x] = (byte)var5;
            if (var5 < this.field_961) {
                this.field_961 = var5;
            } else {
                int var7 = 127;

                for(int var8 = 0; var8 < 16; ++var8) {
                    for(int var9 = 0; var9 < 16; ++var9) {
                        if ((this.heightmap[(var9 << 4) | var8] & 255) < var7) {
                            var7 = this.heightmap[(var9 << 4) | var8] & 255;
                        }
                    }
                }

                this.field_961 = var7;
            }

            int var12 = this.x * 16 + x;
            int var13 = this.z * 16 + z;
            if (var5 < var4) {
                for(int var14 = var5; var14 < var4; ++var14) {
                    this.field_958.method_1704(x, var14, z, 15);
                }
            } else {
                this.level.method_166(LightType.SKY, var12, var4, var13, var12, var5, var13);

                for(int var15 = var4; var15 < var5; ++var15) {
                    this.field_958.method_1704(x, var15, z, 0);
                }
            }

            int var16 = 15;

            int var10;
            for(var10 = var5; var5 > 0 && var16 > 0; this.field_958.method_1704(x, var5, z, var16)) {
                --var5;
                int var11 = BlockBase.LIGHT_OPACITY[this.getTileId(x, var5, z)];
                if (var11 == 0) {
                    var11 = 1;
                }

                var16 -= var11;
                if (var16 < 0) {
                    var16 = 0;
                }
            }

            while(var5 > 0 && BlockBase.LIGHT_OPACITY[this.getTileId(x, var5 - 1, z)] == 0) {
                --var5;
            }

            if (var5 != var10) {
                this.level.method_166(LightType.SKY, var12 - 1, var5, var13 - 1, var12 + 1, var10, var13 + 1);
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
        int var6 = this.heightmap[(z << 4) | x] & 255;
        BlockState oldState = section.getBlockState(x, y & 15, z);
        if (oldState == state && sameMeta)
            return false;
        else {
            int levelX = this.x * 16 + x;
            int levelZ = this.z * 16 + z;
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
        int topY = this.heightmap[(z << 4) | x] & 255;
        BlockState oldState = section.getBlockState(x, y & 15, z);
        if (oldState == state)
            return null;
        else {
            int levelX = this.x * 16 + x;
            int levelZ = this.z * 16 + z;
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
