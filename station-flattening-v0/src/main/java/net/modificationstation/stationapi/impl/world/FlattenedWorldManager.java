package net.modificationstation.stationapi.impl.world;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.modificationstation.stationapi.impl.world.chunk.ChunkSection;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedChunk;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;
import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;
import static net.modificationstation.stationapi.api.util.Identifier.of;

public class FlattenedWorldManager {

    public static final String SECTIONS = of(NAMESPACE, "sections").toString();
    private static final String BLOCK_STATES_KEY = "block_states";
    private static final String METADATA_KEY = "data";
    private static final String SKY_LIGHT_KEY = "sky_light";
    private static final String BLOCK_LIGHT_KEY = "block_light";
    private static final String HEIGHTMAP_KEY = "height_map";
    private static final String HEIGHT_KEY = "y";

    public static void saveChunk(FlattenedChunk chunk, World world, NbtCompound chunkTag) {
        world.checkSessionLock();
        chunkTag.putInt("xPos", chunk.x);
        chunkTag.putInt("zPos", chunk.z);
        chunkTag.putLong("LastUpdate", world.getTime());
        chunk.compactSections();
        ChunkSection[] sections = chunk.sections;
        NbtList sectionTags = new NbtList();
        for (int sectionY = world.getBottomSectionCoord(); sectionY < world.getTopSectionCoord() + 2; ++sectionY) {
            int index = world.sectionCoordToIndex(sectionY);
            if (index < 0 || index >= sections.length) continue;
            ChunkSection section = sections[index];
            if (!ChunkSection.isEmpty(section)) {
                NbtCompound sectionTag = new NbtCompound();
                sectionTag.putByte(HEIGHT_KEY, (byte)sectionY);
                sectionTag.put(BLOCK_STATES_KEY, section.writeBlockStates());
                byte[] metadata = new byte[2048];
                section.copyMetadata(metadata);
                sectionTag.putByteArray(METADATA_KEY, metadata);
                byte[] skyLight = new byte[2048];
                section.copyLight(LightType.SKY, skyLight);
                sectionTag.putByteArray(SKY_LIGHT_KEY, skyLight);
                byte[] blockLight = new byte[2048];
                section.copyLight(LightType.BLOCK, blockLight);
                sectionTag.putByteArray(BLOCK_LIGHT_KEY, blockLight);
                sectionTags.add(sectionTag);
            }
        }
        chunkTag.put(SECTIONS, sectionTags);
        chunkTag.putByteArray(HEIGHTMAP_KEY, chunk.getStoredHeightmap());
        chunkTag.putBoolean("TerrainPopulated", chunk.terrainPopulated);
        chunk.lastSaveHadEntities = false;
        NbtList entityTags = new NbtList();
        for (int i = 0; i < chunk.entities.length; ++i) {
            for (Object object : chunk.entities[i]) {
                chunk.lastSaveHadEntities = true;
                NbtCompound entityTag = new NbtCompound();
                if (!((Entity)object).saveSelfNbt(entityTag)) continue;
                entityTags.add(entityTag);
            }
        }
        chunkTag.put("Entities", entityTags);
        NbtList tileEntityTags = new NbtList();
        for (Object object : chunk.blockEntities.values()) {
            NbtCompound tileEntityTag = new NbtCompound();
            ((BlockEntity)object).writeNbt(tileEntityTag);
            tileEntityTags.add(tileEntityTag);
        }
        chunkTag.put("TileEntities", tileEntityTags);
    }

    public static Chunk loadChunk(World world, NbtCompound chunkTag) {
        int xPos = chunkTag.getInt("xPos");
        int zPos = chunkTag.getInt("zPos");
        FlattenedChunk chunk = new FlattenedChunk(world, xPos, zPos);
        ChunkSection[] sections = chunk.sections;
        if (chunkTag.contains(SECTIONS)) {
            NbtList sectionTags = chunkTag.getList(SECTIONS);
            for (int i = 0; i < sectionTags.size(); i++) {
                NbtCompound sectionTag = (NbtCompound) sectionTags.get(i);
                int sectionY = sectionTag.getByte(HEIGHT_KEY);
                int index = world.sectionCoordToIndex(sectionY);
                if (index < 0 || index >= sections.length) continue;
                ChunkSection chunkSection = new ChunkSection(sectionY);
                if (sectionTag.contains(BLOCK_STATES_KEY))
                    chunkSection.readBlockStates(sectionTag.getCompound(BLOCK_STATES_KEY), errorMessage -> logRecoverableError(xPos, zPos, sectionY, errorMessage));
                chunkSection.setMetadata(sectionTag.getByteArray(METADATA_KEY));
                chunkSection.setLight(LightType.SKY, sectionTag.getByteArray(SKY_LIGHT_KEY));
                chunkSection.setLight(LightType.BLOCK, sectionTag.getByteArray(BLOCK_LIGHT_KEY));
                sections[index] = chunkSection;
            }
        }
        chunk.loadStoredHeightmap(chunkTag.getByteArray(HEIGHTMAP_KEY));
        chunk.terrainPopulated = chunkTag.getBoolean("TerrainPopulated");
        NbtList entityTags = chunkTag.getList("Entities");
        if (entityTags != null) {
            for (int i = 0; i < entityTags.size(); ++i) {
                NbtCompound compoundTag = (NbtCompound) entityTags.get(i);
                Entity object = EntityRegistry.getEntityFromNbt(compoundTag, world);
                chunk.lastSaveHadEntities = true;
                if (object == null) continue;
                chunk.addEntity(object);
            }
        }
        NbtList tileEntityTags = chunkTag.getList("TileEntities");
        if (tileEntityTags != null) {
            for (int i = 0; i < tileEntityTags.size(); ++i) {
                NbtCompound object = (NbtCompound) tileEntityTags.get(i);
                BlockEntity tileEntityBase = BlockEntity.createFromNbt(object);
                if (tileEntityBase == null) continue;
                chunk.addBlockEntity(tileEntityBase);
            }
        }
        return chunk;
    }

    private static void logRecoverableError(int chunkX, int chunkZ, int y, String message) {
        LOGGER.error("Recoverable errors when loading section [" + chunkX + ", " + y + ", " + chunkZ + "]: " + message);
    }
}
