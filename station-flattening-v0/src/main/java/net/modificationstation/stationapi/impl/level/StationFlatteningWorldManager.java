package net.modificationstation.stationapi.impl.level;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockBase;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.EntityRegistry;
import net.minecraft.level.Level;
import net.minecraft.level.LightType;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.tileentity.TileEntityBase;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.io.ListTag;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.States;
import net.modificationstation.stationapi.api.nbt.NbtOps;
import net.modificationstation.stationapi.impl.level.chunk.ChunkSection;
import net.modificationstation.stationapi.impl.level.chunk.PalettedContainer;
import net.modificationstation.stationapi.impl.level.chunk.StationFlatteningChunkImpl;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;
import static net.modificationstation.stationapi.api.StationAPI.MODID;
import static net.modificationstation.stationapi.api.registry.Identifier.of;

public class StationFlatteningWorldManager {

    private static final Codec<PalettedContainer<BlockState>> CODEC = PalettedContainer.createCodec(BlockBase.STATE_IDS, BlockState.CODEC, PalettedContainer.PaletteProvider.BLOCK_STATE, States.AIR.get());
    public static final String SECTIONS = of(MODID, "sections").toString();
    private static final String METADATA_KEY = "data";
    private static final String SKY_LIGHT_KEY = "sky_light";
    private static final String BLOCK_LIGHT_KEY = "block_light";
    private static final String HEIGHTMAP_KEY = "height_map";
    private static final String HEIGHT_KEY = "y";

    public static void saveChunk(StationFlatteningChunkImpl chunk, Level world, CompoundTag chunkTag) {
        world.checkSessionLock();
        chunkTag.put("xPos", chunk.x);
        chunkTag.put("zPos", chunk.z);
        chunkTag.put("LastUpdate", world.getLevelTime());
        ChunkSection[] sections = chunk.sections;
        ListTag sectionTags = new ListTag();
        for (int sectionY = world.getBottomSectionCoord(); sectionY < world.getTopSectionCoord() + 2; ++sectionY) {
            int index = world.sectionCoordToIndex(sectionY);
            if (index < 0 || index >= sections.length) continue;
            ChunkSection section = sections[index];
            if (!ChunkSection.isEmpty(section)) {
                CompoundTag sectionTag = new CompoundTag();
                sectionTag.put(HEIGHT_KEY, (byte)sectionY);
                sectionTag.put("block_states", CODEC.encodeStart(NbtOps.INSTANCE, section.getBlockStateContainer()).getOrThrow(false, LOGGER::error));
                sectionTag.put(METADATA_KEY, section.getMetadataArray().toTag());
                sectionTag.put(SKY_LIGHT_KEY, section.getLightArray(LightType.field_2757).toTag());
                sectionTag.put(BLOCK_LIGHT_KEY, section.getLightArray(LightType.field_2758).toTag());
                sectionTags.add(sectionTag);
            }
        }
        chunkTag.put(SECTIONS, sectionTags);
        chunkTag.put(HEIGHTMAP_KEY, chunk.getStoredHeightmap());
        chunkTag.put("TerrainPopulated", chunk.decorated);
        chunk.field_969 = false;
        ListTag entityTags = new ListTag();
        for (int i = 0; i < chunk.entities.length; ++i) {
            for (Object object : chunk.entities[i]) {
                chunk.field_969 = true;
                CompoundTag entityTag = new CompoundTag();
                if (!((EntityBase)object).method_1343(entityTag)) continue;
                entityTags.add(entityTag);
            }
        }
        chunkTag.put("Entities", entityTags);
        ListTag tileEntityTags = new ListTag();
        for (Object object : chunk.field_964.values()) {
            CompoundTag tileEntityTag = new CompoundTag();
            ((TileEntityBase)object).writeIdentifyingData(tileEntityTag);
            tileEntityTags.add(tileEntityTag);
        }
        chunkTag.put("TileEntities", tileEntityTags);
    }

    public static Chunk loadChunk(Level world, CompoundTag chunkTag) {
        int xPos = chunkTag.getInt("xPos");
        int zPos = chunkTag.getInt("zPos");
        StationFlatteningChunkImpl chunk = new StationFlatteningChunkImpl(world, xPos, zPos);
        ChunkSection[] sections = chunk.sections;
        if (chunkTag.containsKey(SECTIONS)) {
            ListTag sectionTags = chunkTag.getListTag(SECTIONS);
            for (int i = 0; i < sectionTags.size(); i++) {
                CompoundTag sectionTag = (CompoundTag) sectionTags.get(i);
                int sectionY = sectionTag.getByte(HEIGHT_KEY);
                int index = world.sectionCoordToIndex(sectionY);
                if (index < 0 || index >= sections.length) continue;
                PalettedContainer<BlockState> blockStates = sectionTag.containsKey("block_states") ? CODEC.parse(NbtOps.INSTANCE, sectionTag.getCompoundTag("block_states")).promotePartial(errorMessage -> logRecoverableError(xPos, zPos, sectionY, errorMessage)).getOrThrow(false, LOGGER::error) : new PalettedContainer<>(BlockBase.STATE_IDS, States.AIR.get(), PalettedContainer.PaletteProvider.BLOCK_STATE);
                ChunkSection chunkSection = new ChunkSection(sectionY, blockStates);
                chunkSection.getMetadataArray().copyArray(sectionTag.getByteArray(METADATA_KEY));
                chunkSection.getLightArray(LightType.field_2757).copyArray(sectionTag.getByteArray(SKY_LIGHT_KEY));
                chunkSection.getLightArray(LightType.field_2758).copyArray(sectionTag.getByteArray(BLOCK_LIGHT_KEY));
                sections[index] = chunkSection;
            }
        }
        chunk.loadStoredHeightmap(chunkTag.getByteArray(HEIGHTMAP_KEY));
        chunk.decorated = chunkTag.getBoolean("TerrainPopulated");
        ListTag entityTags = chunkTag.getListTag("Entities");
        if (entityTags != null) {
            for (int i = 0; i < entityTags.size(); ++i) {
                CompoundTag compoundTag = (CompoundTag) entityTags.get(i);
                EntityBase object = EntityRegistry.create(compoundTag, world);
                chunk.field_969 = true;
                if (object == null) continue;
                chunk.addEntity(object);
            }
        }
        ListTag tileEntityTags = chunkTag.getListTag("TileEntities");
        if (tileEntityTags != null) {
            for (int i = 0; i < tileEntityTags.size(); ++i) {
                CompoundTag object = (CompoundTag) tileEntityTags.get(i);
                TileEntityBase tileEntityBase = TileEntityBase.tileEntityFromNBT(object);
                if (tileEntityBase == null) continue;
                chunk.method_867(tileEntityBase);
            }
        }
        return chunk;
    }

    private static void logRecoverableError(int chunkX, int chunkZ, int y, String message) {
        LOGGER.error("Recoverable errors when loading section [" + chunkX + ", " + y + ", " + chunkZ + "]: " + message);
    }
}
