package net.modificationstation.stationapi.impl.level.chunk;

import net.minecraft.level.Level;
import net.minecraft.level.LevelProperties;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.level.chunk.ChunkIO;
import net.minecraft.level.storage.RegionLoader;
import net.minecraft.util.io.AbstractTag;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.io.NBTIO;
import net.modificationstation.stationapi.impl.level.StationFlatteningWorldManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import static net.modificationstation.stationapi.impl.level.StationFlatteningWorldManager.SECTIONS;

public class StationFlatteningWorldChunkLoader implements ChunkIO {

    protected final File dimFolder;

    public StationFlatteningWorldChunkLoader(File dimFolder) {
        this.dimFolder = dimFolder;
    }

    @Override
    public Chunk getChunk(Level arg, int i, int j) {
        DataInputStream dataInputStream = RegionLoader.method_1215(dimFolder, i, j);
        if (dataInputStream == null)
            return null;
        CompoundTag compoundTag = NBTIO.readTag(dataInputStream);
        if (!compoundTag.containsKey("Level")) {
            System.out.println("Chunk file at " + i + "," + j + " is missing level data, skipping");
            return null;
        }
        if (!compoundTag.getCompoundTag("Level").containsKey(SECTIONS)) {
            System.out.println("Chunk file at " + i + "," + j + " is missing section data, skipping");
            return null;
        }
        Chunk chunk = StationFlatteningWorldManager.loadChunk(arg, compoundTag.getCompoundTag("Level"));
        if (!chunk.isSameXAndZ(i, j)) {
            System.out.println("Chunk file at " + i + "," + j + " is in the wrong location; relocating. (Expected " + i + ", " + j + ", got " + chunk.x + ", " + chunk.z + ")");
            compoundTag.put("xPos", i);
            compoundTag.put("zPos", j);
            chunk = StationFlatteningWorldManager.loadChunk(arg, compoundTag.getCompoundTag("Level"));
        }
        chunk.method_890();
        return chunk;
    }

    @Override
    public void saveChunk(Level world, Chunk oldChunk) {
        if (!(oldChunk instanceof StationFlatteningChunk chunk)) throw new IllegalStateException(getClass().getSimpleName() + " can't save chunk of type \"" + oldChunk.getClass().getName() + "\"!");
        world.checkSessionLock();
        DataOutputStream dataOutputStream = RegionLoader.method_1216(dimFolder, chunk.x, chunk.z);
        CompoundTag compoundTag = new CompoundTag();
        CompoundTag compoundTag2 = new CompoundTag();
        compoundTag.put("Level", (AbstractTag) compoundTag2);
        StationFlatteningWorldManager.saveChunk(chunk, world, compoundTag2);
        NBTIO.writeTag(compoundTag, dataOutputStream);
        try {
            dataOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        LevelProperties levelProperties = world.getProperties();
        levelProperties.setSizeOnDisk(levelProperties.getSizeOnDisk() + (long) RegionLoader.method_1214(dimFolder, chunk.x, chunk.z));
    }

    @Override
    public void iDoNothingToo(Level arg, Chunk arg2) {}

    @Override
    public void iAmUseless() {}

    @Override
    public void iAmActuallyUseless() {}
}
