package net.modificationstation.stationapi.impl.level;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.class_43;
import net.minecraft.class_56;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.States;
import net.modificationstation.stationapi.api.nbt.NbtOps;
import net.modificationstation.stationapi.impl.level.chunk.ChunkSection;
import net.modificationstation.stationapi.impl.level.chunk.FlattenedChunk;
import net.modificationstation.stationapi.impl.level.chunk.PalettedContainer;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;
import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;
import static net.modificationstation.stationapi.api.util.Identifier.of;

public class FlattenedWorldManager {

    private static final Codec<PalettedContainer<BlockState>> CODEC = PalettedContainer.createCodec(Block.STATE_IDS, BlockState.CODEC, PalettedContainer.PaletteProvider.BLOCK_STATE, States.AIR.get());
    public static final String SECTIONS = of(NAMESPACE, "sections").toString();
    private static final String METADATA_KEY = "data";
    private static final String SKY_LIGHT_KEY = "sky_light";
    private static final String BLOCK_LIGHT_KEY = "block_light";
    private static final String HEIGHTMAP_KEY = "height_map";
    private static final String HEIGHT_KEY = "y";

    public static void saveChunk(FlattenedChunk chunk, World world, NbtCompound chunkTag) {
        world.method_251();
        chunkTag.putInt("xPos", chunk.field_962);
        chunkTag.putInt("zPos", chunk.field_963);
        chunkTag.putLong("LastUpdate", world.getTime());
        ChunkSection[] sections = chunk.sections;
        NbtList sectionTags = new NbtList();
        for (int sectionY = world.getBottomSectionCoord(); sectionY < world.getTopSectionCoord() + 2; ++sectionY) {
            int index = world.sectionCoordToIndex(sectionY);
            if (index < 0 || index >= sections.length) continue;
            ChunkSection section = sections[index];
            if (!ChunkSection.isEmpty(section)) {
                NbtCompound sectionTag = new NbtCompound();
                sectionTag.putByte(HEIGHT_KEY, (byte)sectionY);
                sectionTag.put("block_states", CODEC.encodeStart(NbtOps.INSTANCE, section.getBlockStateContainer()).getOrThrow(false, LOGGER::error));
                sectionTag.put(METADATA_KEY, section.getMetadataArray().toTag());
                sectionTag.put(SKY_LIGHT_KEY, section.getLightArray(class_56.SKY).toTag());
                sectionTag.put(BLOCK_LIGHT_KEY, section.getLightArray(class_56.BLOCK).toTag());
                sectionTags.add(sectionTag);
            }
        }
        chunkTag.put(SECTIONS, sectionTags);
        chunkTag.putByteArray(HEIGHTMAP_KEY, chunk.getStoredHeightmap());
        chunkTag.putBoolean("TerrainPopulated", chunk.field_966);
        chunk.field_969 = false;
        NbtList entityTags = new NbtList();
        for (int i = 0; i < chunk.field_965.length; ++i) {
            for (Object object : chunk.field_965[i]) {
                chunk.field_969 = true;
                NbtCompound entityTag = new NbtCompound();
                if (!((Entity)object).method_1343(entityTag)) continue;
                entityTags.add(entityTag);
            }
        }
        chunkTag.put("Entities", entityTags);
        NbtList tileEntityTags = new NbtList();
        for (Object object : chunk.field_964.values()) {
            NbtCompound tileEntityTag = new NbtCompound();
            ((BlockEntity)object).writeNbt(tileEntityTag);
            tileEntityTags.add(tileEntityTag);
        }
        chunkTag.put("TileEntities", tileEntityTags);
    }

    public static class_43 loadChunk(World world, NbtCompound chunkTag) {
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
                PalettedContainer<BlockState> blockStates = sectionTag.contains("block_states") ? CODEC.parse(NbtOps.INSTANCE, sectionTag.getCompound("block_states")).promotePartial(errorMessage -> logRecoverableError(xPos, zPos, sectionY, errorMessage)).getOrThrow(false, LOGGER::error) : new PalettedContainer<>(Block.STATE_IDS, States.AIR.get(), PalettedContainer.PaletteProvider.BLOCK_STATE);
                ChunkSection chunkSection = new ChunkSection(sectionY, blockStates);
                chunkSection.getMetadataArray().copyArray(sectionTag.getByteArray(METADATA_KEY));
                chunkSection.getLightArray(class_56.SKY).copyArray(sectionTag.getByteArray(SKY_LIGHT_KEY));
                chunkSection.getLightArray(class_56.BLOCK).copyArray(sectionTag.getByteArray(BLOCK_LIGHT_KEY));
                sections[index] = chunkSection;
            }
        }
        chunk.loadStoredHeightmap(chunkTag.getByteArray(HEIGHTMAP_KEY));
        chunk.field_966 = chunkTag.getBoolean("TerrainPopulated");
        NbtList entityTags = chunkTag.getList("Entities");
        if (entityTags != null) {
            for (int i = 0; i < entityTags.size(); ++i) {
                NbtCompound compoundTag = (NbtCompound) entityTags.get(i);
                Entity object = EntityRegistry.getEntityFromNbt(compoundTag, world);
                chunk.field_969 = true;
                if (object == null) continue;
                chunk.method_868(object);
            }
        }
        NbtList tileEntityTags = chunkTag.getList("TileEntities");
        if (tileEntityTags != null) {
            for (int i = 0; i < tileEntityTags.size(); ++i) {
                NbtCompound object = (NbtCompound) tileEntityTags.get(i);
                BlockEntity tileEntityBase = BlockEntity.method_1068(object);
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
