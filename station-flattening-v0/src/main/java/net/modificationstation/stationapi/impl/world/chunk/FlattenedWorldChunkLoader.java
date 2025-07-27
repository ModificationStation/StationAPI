package net.modificationstation.stationapi.impl.world.chunk;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtIo;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ChunkStorage;
import net.minecraft.world.chunk.storage.RegionIo;
import net.modificationstation.stationapi.api.datafixer.TypeReferences;
import net.modificationstation.stationapi.api.nbt.NbtHelper;
import net.modificationstation.stationapi.impl.world.FlattenedWorldManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import static net.modificationstation.stationapi.impl.world.FlattenedWorldManager.SECTIONS;

public class FlattenedWorldChunkLoader implements ChunkStorage {

    protected final File dimFolder;

    public FlattenedWorldChunkLoader(File dimFolder) {
        this.dimFolder = dimFolder;
    }

    @Override
    public Chunk loadChunk(World arg, int i, int j) {
        DataInputStream dataInputStream = RegionIo.getChunkInputStream(dimFolder, i, j);
        if (dataInputStream == null)
            return null;
        NbtCompound compoundTag = NbtIo.read(dataInputStream);
        if (!compoundTag.contains("Level")) {
            System.out.println("Chunk file at " + i + "," + j + " is missing level data, skipping");
            return null;
        }
        compoundTag = NbtHelper.update(TypeReferences.CHUNK, compoundTag);
        if (!compoundTag.getCompound("Level").contains(SECTIONS)) {
            System.out.println("Chunk file at " + i + "," + j + " is missing section data, skipping");
            return null;
        }
        Chunk chunk = FlattenedWorldManager.loadChunk(arg, compoundTag.getCompound("Level"));
        if (!chunk.chunkPosEquals(i, j)) {
            System.out.println("Chunk file at " + i + "," + j + " is in the wrong location; relocating. (Expected " + i + ", " + j + ", got " + chunk.x + ", " + chunk.z + ")");
            compoundTag.putInt("xPos", i);
            compoundTag.putInt("zPos", j);
            chunk = FlattenedWorldManager.loadChunk(arg, compoundTag.getCompound("Level"));
        }
        chunk.fill();
        return chunk;
    }

    @Override
    public void saveChunk(World world, Chunk oldChunk) {
        if (!(oldChunk instanceof FlattenedChunk chunk)) throw new IllegalStateException(getClass().getSimpleName() + " can't save chunk of type \"" + oldChunk.getClass().getName() + "\"!");
        world.checkSessionLock();
        DataOutputStream dataOutputStream = RegionIo.getChunkOutputStream(dimFolder, chunk.x, chunk.z);
        NbtCompound compoundTag = new NbtCompound();
        NbtCompound compoundTag2 = new NbtCompound();
        compoundTag.put("Level", (NbtElement) compoundTag2);
        FlattenedWorldManager.saveChunk(chunk, world, compoundTag2);
        compoundTag = NbtHelper.addDataVersions(compoundTag);
        NbtIo.write(compoundTag, dataOutputStream);
        try {
            dataOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        WorldProperties worldProperties = world.getProperties();
        worldProperties.setSizeOnDisk(worldProperties.getSizeOnDisk() + (long) RegionIo.getChunkSize(dimFolder, chunk.x, chunk.z));
    }

    @Override
    public void saveEntities(World arg, Chunk arg2) {}

    @Override
    public void tick() {}

    @Override
    public void flush() {}
}
