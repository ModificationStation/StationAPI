package net.modificationstation.stationapi.mixin.flattening;

import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockBase;
import net.minecraft.entity.EntityBase;
import net.minecraft.level.Level;
import net.minecraft.level.LightType;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.BlockStateHolder;
import net.modificationstation.stationapi.api.block.States;
import net.modificationstation.stationapi.api.event.block.BlockEvent;
import net.modificationstation.stationapi.api.event.level.BlockSetEvent;
import net.modificationstation.stationapi.api.event.level.MetaSetEvent;
import net.modificationstation.stationapi.api.level.BlockStateView;
import net.modificationstation.stationapi.impl.level.HeightLimitView;
import net.modificationstation.stationapi.impl.level.StationDimension;
import net.modificationstation.stationapi.impl.level.chunk.ChunkSection;
import net.modificationstation.stationapi.impl.level.chunk.ChunkSectionsAccessor;
import net.modificationstation.stationapi.impl.level.chunk.StationHeigtmapProvider;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mixin(Chunk.class)
public abstract class MixinChunk implements ChunkSectionsAccessor, BlockStateView, StationHeigtmapProvider {
    @Shadow public static boolean field_953;
    @Shadow public Level level;
    @Shadow @Final public int x;
    @Shadow @Final public int z;

    @Shadow protected abstract void method_887(int i, int j);

    @Shadow public boolean field_967;
    @Shadow public byte[] tiles;
    @Shadow public int field_961;
    @Shadow public List<EntityBase>[] entities;

    @Unique
    @Getter
    private ChunkSection[] sections;
    @Unique
    private short firstBlock;
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
        StationDimension dimension = (StationDimension) level.dimension;
        sections = new ChunkSection[dimension.getSectionCount()];
        firstBlock = (short) (((HeightLimitView) level).getBottomY());
        lastBlock = (short) (((HeightLimitView) level).getTopY() - 1);
        //noinspection unchecked
        entities = new List[dimension.getSectionCount()];
        for(short i = 0; i < entities.length; i++) {
            this.entities[i] = new ArrayList<>();
        }
    }
    
    @Inject(method = "<init>(Lnet/minecraft/level/Level;[BII)V", at = @At("TAIL"))
    private void onChunkCreation(Level level, byte[] tiles, int x, int z, CallbackInfo info) {
        StationDimension dimension = (StationDimension) level.dimension;
        sections = new ChunkSection[dimension.getSectionCount()];
        firstBlock = (short) (((HeightLimitView) level).getBottomY());
        lastBlock = (short) (((HeightLimitView) level).getTopY() - 1);
        //noinspection unchecked
        entities = new List[dimension.getSectionCount()];
        for(short i = 0; i < entities.length; i++) {
            this.entities[i] = new ArrayList<>();
        }
        for(int i = 0; i < tiles.length; i++) {
            int id = Byte.toUnsignedInt(tiles[i]);
            if (id <= 0 || id >= BlockBase.BY_ID.length) continue;
            int by = i & 127;
            if (by > lastBlock) continue;
            int bx = (i >> 11) & 15;
            int bz = (i >> 7) & 15;
            ChunkSection section = Objects.requireNonNull(getOrCreateSection(by, false));
            BlockStateHolder holder = (BlockStateHolder) BlockBase.BY_ID[id];
            section.setBlockState(bx, by & 15, bz, holder.getDefaultState());
        }
    }
    
    @Inject(method = "getHeight(II)I", at = @At("HEAD"), cancellable = true)
    private void getHeight(int x, int z, CallbackInfoReturnable<Integer> info) {
        info.setReturnValue((int) getShortHeight(x, z));
    }
    
    @Inject(method = "isAboveGround", at = @At("HEAD"), cancellable = true)
    private void isAboveGround(int localX, int y, int localZ, CallbackInfoReturnable<Boolean> info) {
        info.setReturnValue(y >= getShortHeight(localX, localZ));
    }
    
    @Inject(method = "method_864", at = @At("HEAD"), cancellable = true)
    private void getLight(LightType type, int x, int y, int z, CallbackInfoReturnable<Integer> info) {
        ChunkSection section = getSection(y);
        info.setReturnValue(section == null ? type == LightType.field_2757 ? 15 : 0 : section.getLight(type, x, y & 15, z));
    }
    
    @Inject(method = "method_865", at = @At("HEAD"), cancellable = true)
    private void setLight(LightType type, int x, int y, int z, int light, CallbackInfo info) {
        this.field_967 = true;
        ChunkSection section = getOrCreateSection(y, true);
        if (section != null) {
            section.setLight(type, x, y & 15, z, light);
        }
        info.cancel();
    }
    
    @Inject(method = "method_880", at = @At("HEAD"), cancellable = true)
    private void getLightLevel(int x, int y, int z, int light, CallbackInfoReturnable<Integer> info) {
        ChunkSection section = getSection(y);
        int lightLevel = section == null ? 15 : section.getLight(LightType.field_2757, x, y & 15, z);
        if (lightLevel > 0) {
            field_953 = true;
        }
    
        lightLevel -= light;
        int blockLight = section == null ? 0 : section.getLight(LightType.field_2758, x, y & 15, z);
        if (blockLight > lightLevel) {
            lightLevel = blockLight;
        }
        
        info.setReturnValue(lightLevel);
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
                for (height = lastBlock; height > firstBlock; height--) {
                    if (BlockBase.LIGHT_OPACITY[getTileId(x, height - 1, z)] != 0) break;
                }
                setShortHeight(x, z, height);
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
                int y = i & 127;
                if (y > lastBlock) continue;
                int x = (i >> 11) & 15;
                int z = (i >> 7) & 15;
                ChunkSection section = Objects.requireNonNull(getOrCreateSection(y, false));
                BlockStateHolder holder = (BlockStateHolder) BlockBase.BY_ID[id];
                section.setBlockState(x, y & 15, z, holder.getDefaultState());
                tiles[i] = 0;
            }
            tiles = null;
        }
        
        short maxYH;
        int minHeight = lastBlock;
        maxYH = (short) (sections.length - 1);
        while (maxYH >= 0 && sections[maxYH] == null) {
            maxYH--;
        }
        maxYH = (short) ((((HeightLimitView) level).sectionIndexToCoord(maxYH) << 4) + 16);
        
        for (short i = 0; i < 256; i++) {
            byte x = (byte) (i & 15);
            byte z = (byte) (i >> 4);
            short height = maxYH;
            for (short y = maxYH; y >= firstBlock; y--) {
                ChunkSection section = getSection(y);
                if (section != null) {
                    BlockState state = section.getBlockState(x, y & 15, z);
                    int id = state == null ? 0 : state.getBlock().id;
                    if (BlockBase.LIGHT_OPACITY[id] != 0) break;
                    height = y;
                }
            }
            
            stationHeightmap[i] = height;
            if (height < minHeight) {
                minHeight = height;
            }
    
            if (!this.level.dimension.halvesMapping) {
                byte light = 15;
                for (int y = maxYH; y >= firstBlock; y--) {
                    ChunkSection section = getSection(y);
                    if (section != null) {
                        BlockState state = section.getBlockState(x, y & 15, z);
                        int id = state == null ? 0 : state.getBlock().id;
                        light -= BlockBase.LIGHT_OPACITY[id];
                        if (light <= 0) break;
                        section.setLight(LightType.field_2757, x, y & 15, z, light);
                    }
                }
            }
        }

        this.field_961 = minHeight;
    
        for (short i = 0; i < 256; i++) {
            this.method_887(i & 15, i >> 4);
        }

        this.field_967 = true;
    }

    /**
     * @reason early version
     * @author mine_diver
     */
    @Overwrite
    private void method_889(int x, int y, int z) {
        short height = getShortHeight(x, z);
        short maxHeight = (short) Math.max(y, height);
        if (maxHeight > lastBlock) {
            maxHeight = lastBlock;
        }

        while (maxHeight > firstBlock && BlockBase.LIGHT_OPACITY[getTileId(x, maxHeight - 1, z)] == 0) {
            --maxHeight;
        }

        if (maxHeight != height) {
            this.level.method_240(x, z, maxHeight, height);
            setShortHeight(x, z, maxHeight);
            if (maxHeight < this.field_961) {
                this.field_961 = maxHeight;
            }
            else {
                int var7 = lastBlock;
                
                for (int i = 0; i < 256; i++) {
                    short mapHeight = stationHeightmap[i];
                    if (mapHeight < var7) {
                        var7 = mapHeight;
                    }
                }

                this.field_961 = var7;
            }

            int posX = this.x << 4 | x;
            int posZ = this.z << 4 | z;
            
            if (maxHeight < height) {
                for (int h = maxHeight; h < height; ++h) {
                    ChunkSection section = getSection(h);
                    if (section != null) {
                        section.setLight(LightType.field_2757, x, h & 15, z, 15);
                    }
                }
            }
            else {
                this.level.method_166(LightType.field_2757, posX, height, posZ, posX, maxHeight, posZ);
                for (int h = height; h < maxHeight; ++h) {
                    ChunkSection section = getSection(h);
                    if (section != null) {
                        section.setLight(LightType.field_2757, x, h & 15, z, 0);
                    }
                }
            }
            
            int h;
            int light = 15;
            for(h = maxHeight; maxHeight > firstBlock && light > 0;) {
                --maxHeight;
                ChunkSection section = getSection(maxHeight);
                if (section != null) {
                    section.setLight(LightType.field_2757, x, maxHeight & 15, z, light);
                }
                int var11 = BlockBase.LIGHT_OPACITY[this.getTileId(x, maxHeight, z)];
                if (var11 == 0) {
                    var11 = 1;
                }

                light -= var11;
                if (light < 0) {
                    light = 0;
                }
            }

            while(maxHeight > firstBlock && BlockBase.LIGHT_OPACITY[this.getTileId(x, maxHeight - 1, z)] == 0) {
                --maxHeight;
            }

            if (maxHeight != h) {
                this.level.method_166(LightType.field_2757, posX - 1, maxHeight, posZ - 1, posX + 1, h, posZ + 1);
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
        int levelX = this.x << 4 | x;
        int levelZ = this.z << 4 | z;
        BlockSetEvent event =
                BlockSetEvent.builder()
                        .level(level).chunk(Chunk.class.cast(this))
                        .x(levelX).y(y).z(levelZ)
                        .blockState(state).blockMeta(meta)
                        .overrideState(state).overrideMeta(meta)
                        .build();
        if (StationAPI.EVENT_BUS.post(event).isCanceled()) return false;
        state = event.overrideState;
        meta = event.overrideMeta;
        ChunkSection section = getOrCreateSection(y, true);
        if (section == null) return false;
        boolean sameMeta = section.getMeta(x, y & 15, z) == meta;
        if (state.isAir() && sameMeta) return false;
        short var6 = getShortHeight(x, z);
        BlockState oldState = section.getBlockState(x, y & 15, z);
        if (oldState == state && sameMeta) return false;

        BlockBase oldBlock = oldState.getBlock();
        if (
                StationAPI.EVENT_BUS.post(BlockEvent.BeforeRemoved.builder()
                        .block(oldBlock)
                        .level(level)
                        .x(levelX).y(y).z(levelZ)
                        .build()
                ).isCanceled()
        ) return false;
        section.setBlockState(x, y & 15, z, state);
        oldBlock.onBlockRemoved(this.level, levelX, y, levelZ);
        section.setMeta(x, y & 15, z, meta);

        if (!this.level.dimension.halvesMapping) {
            if (BlockBase.LIGHT_OPACITY[state.getBlock().id] != 0) {
                if (y >= var6)
                    this.method_889(x, y + 1, z);
            } else if (y == var6 - 1)
                this.method_889(x, y, z);

            this.level.method_166(LightType.field_2757, levelX, y, levelZ, levelX, y, levelZ);
        }

        this.level.method_166(LightType.field_2758, levelX, y, levelZ, levelX, y, levelZ);
        this.method_887(x, z);
        section.setMeta(x, y & 15, z, meta);
        state.getBlock().onBlockPlaced(this.level, levelX, y, levelZ);

        this.field_967 = true;
        return true;
    }

    /**
     * @reason early version
     * @author mine_diver
     */
    @Overwrite
    public boolean method_860(int x, int y, int z, int blockId) {
        return setBlockState(x, y, z, ((BlockStateHolder) BlockBase.BY_ID[blockId]).getDefaultState()) != null;
    }
    
    @Inject(method = "method_875", at = @At("HEAD"), cancellable = true)
    private void getTileMeta(int x, int y, int z, CallbackInfoReturnable<Integer> info) {
        ChunkSection section = getSection(y);
        info.setReturnValue(section == null ? 0 : section.getMeta(x, y & 15, z));
    }
    
    @Inject(method = "method_876", at = @At("HEAD"), cancellable = true)
    private void setTileMeta(int x, int y, int z, int meta, CallbackInfo info) {
        info.cancel();
        MetaSetEvent event =
                MetaSetEvent.builder()
                        .level(level).chunk(Chunk.class.cast(this))
                        .x(this.x << 4 | x).y(y).z(this.z << 4 | z)
                        .blockMeta(meta)
                        .overrideMeta(meta)
                        .build();
        if (event.isCanceled()) return;
        meta = event.overrideMeta;
        ChunkSection section = getSection(y);
        if (section != null) {
            section.setMeta(x, y & 15, z, meta);
        }
    }

    @Override
    @Unique
    public BlockState getBlockState(int x, int y, int z) {
        ChunkSection section = getSection(y);
        return section == null ? States.AIR.get() : section.getBlockState(x, y & 15, z);
    }

    @Override
    @Unique
    public BlockState setBlockState(int x, int y, int z, BlockState state) {
        int levelX = this.x << 4 | x;
        int levelZ = this.z << 4 | z;
        if (
                StationAPI.EVENT_BUS.post(
                        BlockSetEvent.builder()
                                .level(level).chunk(Chunk.class.cast(this))
                                .x(levelX).y(y).z(levelZ)
                                .blockState(state)
                                .build()
                ).isCanceled()
        ) return null;
        ChunkSection section = getOrCreateSection(y, true);
        if (section == null) return null;
        BlockState oldState = section.getBlockState(x, y & 15, z);
        if (oldState == state) return null;
        
        short topY = getShortHeight(x, z);
        BlockBase oldBlock = oldState.getBlock();
        if (
                StationAPI.EVENT_BUS.post(BlockEvent.BeforeRemoved.builder()
                        .block(oldBlock)
                        .level(level)
                        .x(levelX).y(y).z(levelZ)
                        .build()
                ).isCanceled()
        ) return null;
        section.setBlockState(x, y & 15, z, state);
        oldBlock.onBlockRemoved(this.level, levelX, y, levelZ);
        section.setMeta(x, y & 15, z, 0);
        if (!this.level.dimension.halvesMapping) {
            if (BlockBase.LIGHT_OPACITY[state.getBlock().id] != 0) {
                if (y >= topY)
                    this.method_889(x, y + 1, z);
            } else if (y == topY - 1)
                this.method_889(x, y, z);
            this.level.method_166(LightType.field_2757, levelX, y, levelZ, levelX, y, levelZ);
        }

        this.level.method_166(LightType.field_2758, levelX, y, levelZ, levelX, y, levelZ);
        this.method_887(x, z);
        if (!this.level.isServerSide) {
            state.getBlock().onBlockPlaced(this.level, levelX, y, levelZ);
        }

        this.field_967 = true;
        return oldState;
    }

    @Redirect(
            method = "addEntity(Lnet/minecraft/entity/EntityBase;)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/entity/EntityBase;y:D",
                    opcode = Opcodes.GETFIELD
            )
    )
    private double modifyEntityY(EntityBase instance) {
        return instance.y - ((HeightLimitView) level).getBottomY();
    }

    @Redirect(
            method = {
                    "method_870(Lnet/minecraft/entity/EntityBase;Lnet/minecraft/util/maths/Box;Ljava/util/List;)V",
                    "method_866(Ljava/lang/Class;Lnet/minecraft/util/maths/Box;Ljava/util/List;)V"
            },
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/util/maths/Box;minY:D",
                    opcode = Opcodes.GETFIELD
            )
    )
    private double modifyMinY(Box instance) {
        return instance.minY - ((HeightLimitView) level).getBottomY();
    }

    @Redirect(
            method = {
                    "method_870(Lnet/minecraft/entity/EntityBase;Lnet/minecraft/util/maths/Box;Ljava/util/List;)V",
                    "method_866(Ljava/lang/Class;Lnet/minecraft/util/maths/Box;Ljava/util/List;)V"
            },
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/util/maths/Box;maxY:D",
                    opcode = Opcodes.GETFIELD
            )
    )
    private double modifyMaxY(Box instance) {
        return instance.maxY - ((HeightLimitView) level).getBottomY();
    }

    @Redirect(
            method = "method_890()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/class_490;method_1671([B)V"
            )
    )
    private void stopRemoval(byte[] bs) {}
    
    @Unique
    private ChunkSection getSection(int y) {
        if (y < firstBlock || y > lastBlock) {
            return null;
        }
        return sections[((HeightLimitView) level).sectionCoordToIndex(y >> 4)];
    }
    
    @Unique
    private ChunkSection getOrCreateSection(int y, boolean fillSkyLight) {
        if (y < firstBlock || y > lastBlock) {
            return null;
        }
        int index = ((HeightLimitView) level).sectionCoordToIndex(y >> 4);
        ChunkSection section = sections[index];
        if (section == null) {
            section = new ChunkSection(((HeightLimitView) level).sectionIndexToCoord(index));
            if (fillSkyLight) {
                for (short i = 0; i < 4096; i++) {
                    section.setLight(LightType.field_2757, i, 15);
                }
            }
            sections[index] = section;
        }
        return section;
    }
    
    @Unique
    private short getShortHeight(int x, int z) {
        return stationHeightmap[z << 4 | x];
    }
    
    @Unique
    private void setShortHeight(int x, int z, short height) {
        stationHeightmap[z << 4 | x] = height;
    }
}
