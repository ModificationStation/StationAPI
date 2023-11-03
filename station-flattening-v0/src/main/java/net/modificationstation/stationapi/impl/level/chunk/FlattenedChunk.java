package net.modificationstation.stationapi.impl.level.chunk;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.class_43;
import net.minecraft.class_56;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.States;
import net.modificationstation.stationapi.api.event.block.BlockEvent;
import net.modificationstation.stationapi.api.event.level.BlockSetEvent;
import net.modificationstation.stationapi.api.event.level.MetaSetEvent;
import net.modificationstation.stationapi.mixin.flattening.ChunkAccessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FlattenedChunk extends class_43 {

    public final ChunkSection[] sections;
    public final short firstBlock;
    public final short lastBlock;
    private final short[] stationHeightmap = new short[256];

    public FlattenedChunk(World world, int xPos, int zPos) {
        super(world, xPos, zPos);
        int countSections = field_956.countVerticalSections();
        sections = new ChunkSection[countSections];
        firstBlock = (short) field_956.getBottomY();
        lastBlock = (short) (field_956.getTopY() - 1);
        field_965 = new List[countSections];
        for(short i = 0; i < field_965.length; i++) {
            this.field_965[i] = new ArrayList<>();
        }
    }

    public void fromLegacy(byte[] tiles) {
        int mask = (tiles.length >> 8) - 1;
        int offsetZ = mask == 127 ? 7 : net.modificationstation.stationapi.api.util.math.MathHelper.ceilLog2(mask + 1);
        int offsetX = offsetZ + 4;
        int id, bx, by, bz;
        for(int i = 0; i < tiles.length; i++) {
            id = Byte.toUnsignedInt(tiles[i]);
            if (id == 0 || id >= Block.BLOCKS.length) continue;
            by = i & mask;
            if (by > lastBlock) continue;
            bx = (i >> offsetX) & 15;
            bz = (i >> offsetZ) & 15;
            Objects.requireNonNull(getOrCreateSection(by, false)).setBlockState(bx, by & 15, bz, Block.BLOCKS[id].getDefaultState());
        }
    }

    public byte[] getStoredHeightmap() {
        byte[] heightmap = new byte[512];
        for (short i = 0; i < 512; i++) {
            short value = stationHeightmap[i >> 1];
            byte val = (i & 1) == 0 ? (byte) (value & 255) : (byte) ((value >> 8) & 255);
            heightmap[i] = val;
        }
        return heightmap;
    }

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

    private short getShortHeight(int x, int z) {
        return stationHeightmap[z << 4 | x];
    }

    @Override
    public int method_874(int x, int z) {
        return getShortHeight(x, z);
    }

    @Override
    public boolean method_879(int relX, int y, int relZ) {
        return y >= getShortHeight(relX, relZ);
    }

    private ChunkSection getSection(int y) {
        if (y < firstBlock || y > lastBlock) {
            return null;
        }
        return sections[field_956.sectionCoordToIndex(y >> 4)];
    }

    @Override
    public int method_864(class_56 type, int x, int y, int z) {
        ChunkSection section = getSection(y);
        return section == null ?
                type == class_56.field_2757 && field_956.dimension.field_2177 ?
                        0 :
                        type.field_2759 :
                section.getLight(type, x, y & 15, z);
    }

    public ChunkSection getOrCreateSection(int y, boolean fillSkyLight) {
        if (y < firstBlock || y > lastBlock) {
            return null;
        }
        int index = field_956.sectionCoordToIndex(y >> 4);
        ChunkSection section = sections[index];
        if (section == null) {
            section = new ChunkSection(field_956.sectionIndexToCoord(index));
            if (!field_956.dimension.field_2177 && fillSkyLight) {
                section.initSkyLight();
            }
            sections[index] = section;
        }
        return section;
    }

    @Override
    public void method_865(class_56 type, int x, int y, int z, int light) {
        this.field_967 = true;
        ChunkSection section = getOrCreateSection(y, true);
        if (section != null) {
            section.setLight(type, x, y & 15, z, light);
        }
    }

    @Override
    public int method_880(int x, int y, int z, int light) {
        ChunkSection section = getSection(y);
        int lightLevel = section == null ? 15 : section.getLight(class_56.field_2757, x, y & 15, z);
        if (lightLevel > 0) {
            field_953 = true;
        }

        lightLevel -= light;
        int blockLight = section == null ? 0 : section.getLight(class_56.field_2758, x, y & 15, z);
        if (blockLight > lightLevel) {
            lightLevel = blockLight;
        }

        return lightLevel;
    }

    private void setShortHeight(int x, int z, short height) {
        stationHeightmap[z << 4 | x] = height;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void method_892() {
        int chunkHeight = lastBlock;

        for(int x = 0; x < 16; ++x) {
            for(int z = 0; z < 16; ++z) {
                short height;
                for (height = lastBlock; height > firstBlock; height--) {
                    if (Block.BLOCKS_LIGHT_OPACITY[method_859(x, height - 1, z)] != 0) break;
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

    @Override
    public void method_873() {
        int minHeight = lastBlock;
        for(int x = 0; x < 16; ++x) {
            for(int z = 0; z < 16; ++z) {
                short height = lastBlock;

                while (height > firstBlock) {
                    if (Block.BLOCKS_LIGHT_OPACITY[method_859(x, height - 1, z)] != 0) break;
                    --height;
                }

                setShortHeight(x, z, height);
                if (height < minHeight) {
                    minHeight = height;
                }

                if (!this.field_956.dimension.field_2177) {
                    int light = 15;
                    int lightY = lastBlock;

                    do {
                        light -= Block.BLOCKS_LIGHT_OPACITY[method_859(x, lightY, z)];
                        if (light > 0) {
                            ChunkSection section = getSection(lightY);
                            if (section != null) {
                                section.setLight(class_56.field_2757, x, lightY & 15, z, light);
                            }
                        }

                        --lightY;
                    } while (lightY > firstBlock && light > 0);
                }
            }
        }

        this.field_961 = minHeight;

        for (short i = 0; i < 256; i++) {
            ((ChunkAccessor) this).invokeMethod_887(i & 15, i >> 4);
        }

        this.field_967 = true;
    }

    private void method_889(int x, int y, int z) {
        short height = getShortHeight(x, z);
        short maxHeight = (short) Math.max(y, height);
        if (maxHeight > lastBlock) {
            maxHeight = lastBlock;
        }

        while (maxHeight > firstBlock && Block.BLOCKS_LIGHT_OPACITY[method_859(x, maxHeight - 1, z)] == 0) {
            --maxHeight;
        }

        if (maxHeight != height) {
            this.field_956.method_240(x, z, maxHeight, height);
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

            int posX = this.field_962 << 4 | x;
            int posZ = this.field_963 << 4 | z;

            if (maxHeight < height) {
                for (int h = maxHeight; h < height; ++h) {
                    ChunkSection section = getSection(h);
                    if (section != null) {
                        section.setLight(class_56.field_2757, x, h & 15, z, 15);
                    }
                }
            }
            else {
                this.field_956.method_166(class_56.field_2757, posX, height, posZ, posX, maxHeight, posZ);
                for (int h = height; h < maxHeight; ++h) {
                    ChunkSection section = getSection(h);
                    if (section != null) {
                        section.setLight(class_56.field_2757, x, h & 15, z, 0);
                    }
                }
            }

            int h;
            int light = 15;
            for(h = maxHeight; maxHeight > firstBlock && light > 0;) {
                --maxHeight;
                int var11 = Block.BLOCKS_LIGHT_OPACITY[this.method_859(x, maxHeight, z)];
                if (var11 == 0) {
                    var11 = 1;
                }

                light -= var11;
                if (light < 0) {
                    light = 0;
                }
                ChunkSection section = getSection(maxHeight);
                if (section != null) {
                    section.setLight(class_56.field_2757, x, maxHeight & 15, z, light);
                }
            }

            while(maxHeight > firstBlock && Block.BLOCKS_LIGHT_OPACITY[this.method_859(x, maxHeight - 1, z)] == 0) {
                --maxHeight;
            }

            if (maxHeight != h) {
                this.field_956.method_166(class_56.field_2757, posX - 1, maxHeight, posZ - 1, posX + 1, h, posZ + 1);
            }

            this.field_967 = true;
        }
    }

    @Override
    public int method_859(int x, int y, int z) {
        return getBlockState(x, y, z).getBlock().id;
    }

    @Override
    public boolean method_861(int x, int y, int z, int blockId, int meta) {
        return setBlockStateWithMetadata(x, y, z, Block.BLOCKS[blockId].getDefaultState(), meta) != null;
    }

    @Override
    public boolean method_860(int x, int y, int z, int blockId) {
        return setBlockState(x, y, z, Block.BLOCKS[blockId].getDefaultState()) != null;
    }

    @Override
    public int method_875(int x, int y, int z) {
        ChunkSection section = getSection(y);
        return section == null ? 0 : section.getMeta(x, y & 15, z);
    }

    @Override
    public void method_876(int x, int y, int z, int meta) {
        MetaSetEvent event =
                MetaSetEvent.builder()
                        .level(field_956).chunk(this)
                        .x(this.field_962 << 4 | x).y(y).z(this.field_963 << 4 | z)
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
    public BlockState getBlockState(int x, int y, int z) {
        ChunkSection section = getSection(y);
        if (section == null) return States.AIR.get();
        BlockState blockState = section.getBlockState(x, y & 15, z);
        if (blockState == null)
            throw new RuntimeException();
        return blockState;
    }

    @Override
    public BlockState setBlockStateWithMetadata(int x, int y, int z, BlockState state, int meta) {
        int levelX = this.field_962 << 4 | x;
        int levelZ = this.field_963 << 4 | z;
        BlockSetEvent event =
                BlockSetEvent.builder()
                        .level(field_956).chunk(this)
                        .x(levelX).y(y).z(levelZ)
                        .blockState(state).blockMeta(meta)
                        .overrideState(state).overrideMeta(meta)
                        .build();
        if (StationAPI.EVENT_BUS.post(event).isCanceled()) return null;
        state = event.overrideState;
        meta = event.overrideMeta;
        ChunkSection section = getOrCreateSection(y, true);
        if (section == null) return null;
        boolean sameMeta = section.getMeta(x, y & 15, z) == meta;
        short var6 = getShortHeight(x, z);
        BlockState oldState = section.getBlockState(x, y & 15, z);
        if (oldState == state && sameMeta) return null;

        Block oldBlock = oldState.getBlock();
        if (
                StationAPI.EVENT_BUS.post(BlockEvent.BeforeRemoved.builder()
                        .block(oldBlock)
                        .level(field_956)
                        .x(levelX).y(y).z(levelZ)
                        .build()
                ).isCanceled()
        ) return null;
        section.setBlockState(x, y & 15, z, state);
        if (!field_956.isRemote)
            oldBlock.onBreak(this.field_956, levelX, y, levelZ);
        section.setMeta(x, y & 15, z, meta);

        if (!this.field_956.dimension.field_2177) {
            if (Block.BLOCKS_LIGHT_OPACITY[state.getBlock().id] != 0) {
                if (y >= var6)
                    this.method_889(x, y + 1, z);
            } else if (y == var6 - 1)
                this.method_889(x, y, z);

            this.field_956.method_166(class_56.field_2757, levelX, y, levelZ, levelX, y, levelZ);
        }

        this.field_956.method_166(class_56.field_2758, levelX, y, levelZ, levelX, y, levelZ);
        ((ChunkAccessor) this).invokeMethod_887(x, z);
        section.setMeta(x, y & 15, z, meta);
        state.getBlock().onBlockPlaced(this.field_956, levelX, y, levelZ, oldState);

        this.field_967 = true;
        return oldState;
    }

    @Override
    public BlockState setBlockState(int x, int y, int z, BlockState state) {
        int levelX = this.field_962 << 4 | x;
        int levelZ = this.field_963 << 4 | z;
        if (
                StationAPI.EVENT_BUS.post(
                        BlockSetEvent.builder()
                                .level(field_956).chunk(this)
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
        Block oldBlock = oldState.getBlock();
        if (
                StationAPI.EVENT_BUS.post(BlockEvent.BeforeRemoved.builder()
                        .block(oldBlock)
                        .level(field_956)
                        .x(levelX).y(y).z(levelZ)
                        .build()
                ).isCanceled()
        ) return null;
        section.setBlockState(x, y & 15, z, state);
        oldBlock.onBreak(this.field_956, levelX, y, levelZ);
        section.setMeta(x, y & 15, z, 0);
        if (Block.BLOCKS_LIGHT_OPACITY[state.getBlock().id] != 0) {
            if (y >= topY)
                this.method_889(x, y + 1, z);
        } else if (y == topY - 1)
            this.method_889(x, y, z);
        this.field_956.method_166(class_56.field_2757, levelX, y, levelZ, levelX, y, levelZ);
        this.field_956.method_166(class_56.field_2758, levelX, y, levelZ, levelX, y, levelZ);
        ((ChunkAccessor) this).invokeMethod_887(x, z);
        if (!this.field_956.isRemote) {
            state.getBlock().onBlockPlaced(this.field_956, levelX, y, levelZ, oldState);
        }

        this.field_967 = true;
        return oldState;
    }

    @Override
    public void method_868(Entity entity) {
        int n;
        this.field_969 = true;
        int n2 = MathHelper.floor(entity.x / 16.0);
        int n3 = MathHelper.floor(entity.z / 16.0);
        if (n2 != this.field_962 || n3 != this.field_963) {
            System.out.println("Wrong location! " + entity);
            Thread.dumpStack();
        }
        if ((n = MathHelper.floor((entity.y - field_956.getBottomY()) / 16.0)) < 0) {
            n = 0;
        }
        if (n >= this.field_965.length) {
            n = this.field_965.length - 1;
        }
        entity.field_1618 = true;
        entity.field_1619 = this.field_962;
        entity.field_1620 = n;
        entity.field_1621 = this.field_963;
        //noinspection unchecked
        this.field_965[n].add(entity);
    }

    @Override
    public void method_870(Entity entity, Box box, List entities) {
        int n = MathHelper.floor((box.minY - field_956.getBottomY() - 2.0) / 16.0);
        int n2 = MathHelper.floor((box.maxY - field_956.getBottomY() + 2.0) / 16.0);
        if (n < 0) {
            n = 0;
        }
        if (n2 >= this.field_965.length) {
            n2 = this.field_965.length - 1;
        }
        for (int i = n; i <= n2; ++i) {
            //noinspection unchecked
            List<Entity> list2 = this.field_965[i];
            for (int j = 0; j < list2.size(); ++j) {
                Entity entityBase = list2.get(j);
                if (entityBase == entity || !entityBase.boundingBox.intersects(box)) continue;
                //noinspection unchecked
                entities.add(entityBase);
            }
        }
    }

    @Override
    public void method_866(Class class_, Box arg, List list) {
        int n = MathHelper.floor((arg.minY - field_956.getBottomY() - 2.0) / 16.0);
        int n2 = MathHelper.floor((arg.maxY - field_956.getBottomY() + 2.0) / 16.0);
        if (n < 0) {
            n = 0;
        }
        if (n2 >= this.field_965.length) {
            n2 = this.field_965.length - 1;
        }
        for (int i = n; i <= n2; ++i) {
            //noinspection unchecked
            List<Entity> list2 = this.field_965[i];
            for (int j = 0; j < list2.size(); ++j) {
                Entity entityBase = list2.get(j);
                //noinspection unchecked
                if (!class_.isAssignableFrom(entityBase.getClass()) || !entityBase.boundingBox.intersects(arg)) continue;
                //noinspection unchecked
                list.add(entityBase);
            }
        }
    }

    @Override
    public void method_890() {}

    @Override
    public void method_862(int relX, int relY, int relZ, BlockEntity arg) {
        BlockPos tilePos = new BlockPos(relX, relY, relZ);
        arg.world = this.field_956;
        arg.x = this.field_962 * 16 + relX;
        arg.y = relY;
        arg.z = this.field_963 * 16 + relZ;
        if (this.method_859(relX, relY, relZ) == 0 || !Block.BLOCKS_WITH_ENTITY[this.method_859(relX, relY, relZ)]) {
            System.out.println("Attempted to place a tile entity where there was no entity tile!");
            return;
        }
        arg.method_1073();
        //noinspection unchecked
        this.field_964.put(tilePos, arg);
    }
}
