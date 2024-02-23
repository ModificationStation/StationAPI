package net.modificationstation.stationapi.impl.world.chunk;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.States;
import net.modificationstation.stationapi.api.event.block.BlockEvent;
import net.modificationstation.stationapi.api.event.world.BlockSetEvent;
import net.modificationstation.stationapi.api.event.world.MetaSetEvent;
import net.modificationstation.stationapi.mixin.flattening.ChunkAccessor;

import java.util.ArrayList;
import java.util.List;

public class FlattenedChunk extends Chunk {

    public final ChunkSection[] sections;
    public final short firstBlock;
    public final short lastBlock;
    private final short[] stationHeightmap = new short[256];

    public FlattenedChunk(World world, int xPos, int zPos) {
        super(world, xPos, zPos);
        int countSections = world.countVerticalSections();
        sections = new ChunkSection[countSections];
        firstBlock = (short) world.getBottomY();
        lastBlock = (short) (world.getTopY() - 1);
        entities = new List[countSections];
        for(short i = 0; i < entities.length; i++) {
            this.entities[i] = new ArrayList<>();
        }
    }

    public void fromLegacy(byte[] tiles) {
        int mask = (tiles.length >> 8) - 1;
        int offsetZ = mask == 127 ? 7 : net.modificationstation.stationapi.api.util.math.MathHelper.ceilLog2(mask + 1);
        int offsetX = offsetZ + 4;
        int bottom = this.world.getBottomY();
        int id, bx, by, bz;
        for(int i = 0; i < tiles.length; i++) {
            id = Byte.toUnsignedInt(tiles[i]);
            if (id == 0 || id >= Block.BLOCKS.length) continue;
            by = (i & mask) + bottom;
            ChunkSection section = getOrCreateSection(by, false);
            if (section == null) continue;
            bx = (i >> offsetX) & 15;
            bz = (i >> offsetZ) & 15;
            section.setBlockState(bx, by & 15, bz, Block.BLOCKS[id].getDefaultState());
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
    public int getHeight(int x, int z) {
        return getShortHeight(x, z);
    }

    @Override
    public boolean isAboveMaxHeight(int relX, int y, int relZ) {
        return y >= getShortHeight(relX, relZ);
    }

    private ChunkSection getSection(int y) {
        if (y < firstBlock || y > lastBlock) {
            return null;
        }
        return sections[world.sectionCoordToIndex(y >> 4)];
    }

    @Override
    public int getLight(LightType type, int x, int y, int z) {
        ChunkSection section = getSection(y);
        return section == null ?
                type == LightType.SKY && world.dimension.field_2177 ?
                        0 :
                        type.defaultValue :
                section.getLight(type, x, y & 15, z);
    }

    public ChunkSection getOrCreateSection(int y, boolean fillSkyLight) {
        if (y < firstBlock || y > lastBlock) {
            return null;
        }
        int index = world.sectionCoordToIndex(y >> 4);
        ChunkSection section = sections[index];
        if (section == null) {
            section = new ChunkSection(world.sectionIndexToCoord(index));
            if (!world.dimension.field_2177 && fillSkyLight) {
                section.initSkyLight();
            }
            sections[index] = section;
        }
        return section;
    }

    @Override
    public void setLight(LightType type, int x, int y, int z, int light) {
        this.field_967 = true;
        ChunkSection section = getOrCreateSection(y, true);
        if (section != null) {
            section.setLight(type, x, y & 15, z, light);
        }
    }

    @Override
    public int getLight(int x, int y, int z, int light) {
        ChunkSection section = getSection(y);
        int lightLevel = section == null ? 15 : section.getLight(LightType.SKY, x, y & 15, z);
        if (lightLevel > 0) {
            field_953 = true;
        }

        lightLevel -= light;
        int blockLight = section == null ? 0 : section.getLight(LightType.BLOCK, x, y & 15, z);
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
    public void populateHeightmap() {
        int chunkHeight = lastBlock;

        for(int x = 0; x < 16; ++x) {
            for(int z = 0; z < 16; ++z) {
                short height;
                for (height = lastBlock; height > firstBlock; height--) {
                    if (Block.BLOCKS_LIGHT_OPACITY[getBlockId(x, height - 1, z)] != 0) break;
                }
                setShortHeight(x, z, height);
                if (height < chunkHeight) {
                    chunkHeight = height;
                }
            }
        }

        this.minHeightmapValue = chunkHeight;
        this.field_967 = true;
    }

    @Override
    public void method_873() {
        int minHeight = lastBlock;
        for(int x = 0; x < 16; ++x) {
            for(int z = 0; z < 16; ++z) {
                short height = lastBlock;

                while (height > firstBlock) {
                    if (Block.BLOCKS_LIGHT_OPACITY[getBlockId(x, height - 1, z)] != 0) break;
                    --height;
                }

                setShortHeight(x, z, height);
                if (height < minHeight) {
                    minHeight = height;
                }

                if (!this.world.dimension.field_2177) {
                    int light = 15;
                    int lightY = lastBlock;

                    do {
                        light -= Block.BLOCKS_LIGHT_OPACITY[getBlockId(x, lightY, z)];
                        if (light > 0) {
                            ChunkSection section = getSection(lightY);
                            if (section != null) {
                                section.setLight(LightType.SKY, x, lightY & 15, z, light);
                            }
                        }

                        --lightY;
                    } while (lightY > firstBlock && light > 0);
                }
            }
        }

        this.minHeightmapValue = minHeight;

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

        while (maxHeight > firstBlock && Block.BLOCKS_LIGHT_OPACITY[getBlockId(x, maxHeight - 1, z)] == 0) {
            --maxHeight;
        }

        if (maxHeight != height) {
            this.world.method_240(x, z, maxHeight, height);
            setShortHeight(x, z, maxHeight);
            if (maxHeight < this.minHeightmapValue) {
                this.minHeightmapValue = maxHeight;
            }
            else {
                int var7 = lastBlock;

                for (int i = 0; i < 256; i++) {
                    short mapHeight = stationHeightmap[i];
                    if (mapHeight < var7) {
                        var7 = mapHeight;
                    }
                }

                this.minHeightmapValue = var7;
            }

            int posX = this.x << 4 | x;
            int posZ = this.z << 4 | z;

            if (maxHeight < height) {
                for (int h = maxHeight; h < height; ++h) {
                    ChunkSection section = getSection(h);
                    if (section != null) {
                        section.setLight(LightType.SKY, x, h & 15, z, 15);
                    }
                }
            }
            else {
                this.world.method_166(LightType.SKY, posX, height, posZ, posX, maxHeight, posZ);
                for (int h = height; h < maxHeight; ++h) {
                    ChunkSection section = getSection(h);
                    if (section != null) {
                        section.setLight(LightType.SKY, x, h & 15, z, 0);
                    }
                }
            }

            int h;
            int light = 15;
            for(h = maxHeight; maxHeight > firstBlock && light > 0;) {
                --maxHeight;
                int var11 = Block.BLOCKS_LIGHT_OPACITY[this.getBlockId(x, maxHeight, z)];
                if (var11 == 0) {
                    var11 = 1;
                }

                light -= var11;
                if (light < 0) {
                    light = 0;
                }
                ChunkSection section = getSection(maxHeight);
                if (section != null) {
                    section.setLight(LightType.SKY, x, maxHeight & 15, z, light);
                }
            }

            while(maxHeight > firstBlock && Block.BLOCKS_LIGHT_OPACITY[this.getBlockId(x, maxHeight - 1, z)] == 0) {
                --maxHeight;
            }

            if (maxHeight != h) {
                this.world.method_166(LightType.SKY, posX - 1, maxHeight, posZ - 1, posX + 1, h, posZ + 1);
            }

            this.field_967 = true;
        }
    }

    @Override
    public int getBlockId(int x, int y, int z) {
        return getBlockState(x, y, z).getBlock().id;
    }

    @Override
    public boolean setBlock(int x, int y, int z, int blockId, int meta) {
        return setBlockStateWithMetadata(x, y, z, Block.BLOCKS[blockId].getDefaultState(), meta) != null;
    }

    @Override
    public boolean setBlockId(int x, int y, int z, int blockId) {
        return setBlockState(x, y, z, Block.BLOCKS[blockId].getDefaultState()) != null;
    }

    @Override
    public int getBlockMeta(int x, int y, int z) {
        ChunkSection section = getSection(y);
        return section == null ? 0 : section.getMeta(x, y & 15, z);
    }

    @Override
    public void setBlockMeta(int x, int y, int z, int meta) {
        MetaSetEvent event =
                MetaSetEvent.builder()
                        .world(world).chunk(this)
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
        int worldX = this.x << 4 | x;
        int worldZ = this.z << 4 | z;
        BlockSetEvent event =
                BlockSetEvent.builder()
                        .world(world).chunk(this)
                        .x(worldX).y(y).z(worldZ)
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
                        .world(world)
                        .x(worldX).y(y).z(worldZ)
                        .build()
                ).isCanceled()
        ) return null;
        section.setBlockState(x, y & 15, z, state);
        if (!world.isRemote)
            oldBlock.onBreak(this.world, worldX, y, worldZ);
        section.setMeta(x, y & 15, z, meta);

        if (!this.world.dimension.field_2177) {
            if (Block.BLOCKS_LIGHT_OPACITY[state.getBlock().id] != 0) {
                if (y >= var6)
                    this.method_889(x, y + 1, z);
            } else if (y == var6 - 1)
                this.method_889(x, y, z);

            this.world.method_166(LightType.SKY, worldX, y, worldZ, worldX, y, worldZ);
        }

        this.world.method_166(LightType.BLOCK, worldX, y, worldZ, worldX, y, worldZ);
        ((ChunkAccessor) this).invokeMethod_887(x, z);
        section.setMeta(x, y & 15, z, meta);
        state.getBlock().onBlockPlaced(this.world, worldX, y, worldZ, oldState);

        this.field_967 = true;
        return oldState;
    }

    @Override
    public BlockState setBlockState(int x, int y, int z, BlockState state) {
        int worldX = this.x << 4 | x;
        int worldZ = this.z << 4 | z;
        if (
                StationAPI.EVENT_BUS.post(
                        BlockSetEvent.builder()
                                .world(world).chunk(this)
                                .x(worldX).y(y).z(worldZ)
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
                        .world(world)
                        .x(worldX).y(y).z(worldZ)
                        .build()
                ).isCanceled()
        ) return null;
        section.setBlockState(x, y & 15, z, state);
        oldBlock.onBreak(this.world, worldX, y, worldZ);
        section.setMeta(x, y & 15, z, 0);
        if (Block.BLOCKS_LIGHT_OPACITY[state.getBlock().id] != 0) {
            if (y >= topY)
                this.method_889(x, y + 1, z);
        } else if (y == topY - 1)
            this.method_889(x, y, z);
        this.world.method_166(LightType.SKY, worldX, y, worldZ, worldX, y, worldZ);
        this.world.method_166(LightType.BLOCK, worldX, y, worldZ, worldX, y, worldZ);
        ((ChunkAccessor) this).invokeMethod_887(x, z);
        if (!this.world.isRemote) {
            state.getBlock().onBlockPlaced(this.world, worldX, y, worldZ, oldState);
        }

        this.field_967 = true;
        return oldState;
    }

    @Override
    public void addEntity(Entity entity) {
        int n;
        this.field_969 = true;
        int n2 = MathHelper.floor(entity.x / 16.0);
        int n3 = MathHelper.floor(entity.z / 16.0);
        if (n2 != this.x || n3 != this.z) {
            System.out.println("Wrong location! " + entity);
            Thread.dumpStack();
        }
        if ((n = MathHelper.floor((entity.y - world.getBottomY()) / 16.0)) < 0) {
            n = 0;
        }
        if (n >= this.entities.length) {
            n = this.entities.length - 1;
        }
        entity.field_1618 = true;
        entity.chunkX = this.x;
        entity.chunkY = n;
        entity.chunkZ = this.z;
        //noinspection unchecked
        this.entities[n].add(entity);
    }

    @Override
    public void collectOtherEntities(Entity entity, Box box, List entities) {
        int n = MathHelper.floor((box.minY - world.getBottomY() - 2.0) / 16.0);
        int n2 = MathHelper.floor((box.maxY - world.getBottomY() + 2.0) / 16.0);
        if (n < 0) {
            n = 0;
        }
        if (n2 >= this.entities.length) {
            n2 = this.entities.length - 1;
        }
        for (int i = n; i <= n2; ++i) {
            //noinspection unchecked
            List<Entity> list2 = this.entities[i];
            for (int j = 0; j < list2.size(); ++j) {
                Entity entityBase = list2.get(j);
                if (entityBase == entity || !entityBase.boundingBox.intersects(box)) continue;
                //noinspection unchecked
                entities.add(entityBase);
            }
        }
    }

    @Override
    public void collectEntitiesByClass(Class class_, Box arg, List list) {
        int n = MathHelper.floor((arg.minY - world.getBottomY() - 2.0) / 16.0);
        int n2 = MathHelper.floor((arg.maxY - world.getBottomY() + 2.0) / 16.0);
        if (n < 0) {
            n = 0;
        }
        if (n2 >= this.entities.length) {
            n2 = this.entities.length - 1;
        }
        for (int i = n; i <= n2; ++i) {
            //noinspection unchecked
            List<Entity> list2 = this.entities[i];
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
    public void setBlockEntity(int relX, int relY, int relZ, BlockEntity arg) {
        BlockPos tilePos = new BlockPos(relX, relY, relZ);
        arg.world = this.world;
        arg.x = this.x * 16 + relX;
        arg.y = relY;
        arg.z = this.z * 16 + relZ;
        if (this.getBlockId(relX, relY, relZ) == 0 || !Block.BLOCKS_WITH_ENTITY[this.getBlockId(relX, relY, relZ)]) {
            System.out.println("Attempted to place a tile entity where there was no entity tile!");
            return;
        }
        arg.method_1073();
        //noinspection unchecked
        this.blockEntities.put(tilePos, arg);
    }
}
