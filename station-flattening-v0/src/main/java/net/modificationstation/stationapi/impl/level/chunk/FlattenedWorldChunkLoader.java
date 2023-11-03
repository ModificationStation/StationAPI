package net.modificationstation.stationapi.impl.level.chunk;

import net.minecraft.class_243;
import net.minecraft.class_379;
import net.minecraft.class_43;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtIo;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;
import net.modificationstation.stationapi.api.datafixer.TypeReferences;
import net.modificationstation.stationapi.api.nbt.NbtHelper;
import net.modificationstation.stationapi.impl.level.FlattenedWorldManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import static net.modificationstation.stationapi.impl.level.FlattenedWorldManager.SECTIONS;

public class FlattenedWorldChunkLoader implements class_243 {

    protected final File dimFolder;

    public FlattenedWorldChunkLoader(File dimFolder) {
        this.dimFolder = dimFolder;
    }

    @Override
    public class_43 method_811(World arg, int i, int j) {
        DataInputStream dataInputStream = class_379.method_1215(dimFolder, i, j);
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
        class_43 chunk = FlattenedWorldManager.loadChunk(arg, compoundTag.getCompound("Level"));
        if (!chunk.method_858(i, j)) {
            System.out.println("Chunk file at " + i + "," + j + " is in the wrong location; relocating. (Expected " + i + ", " + j + ", got " + chunk.field_962 + ", " + chunk.field_963 + ")");
            compoundTag.putInt("xPos", i);
            compoundTag.putInt("zPos", j);
            chunk = FlattenedWorldManager.loadChunk(arg, compoundTag.getCompound("Level"));
        }
        chunk.method_890();
        return chunk;
    }

    @Override
    public void method_812(World world, class_43 oldChunk) {
        if (!(oldChunk instanceof FlattenedChunk chunk)) throw new IllegalStateException(getClass().getSimpleName() + " can't save chunk of type \"" + oldChunk.getClass().getName() + "\"!");
        world.method_251();
        DataOutputStream dataOutputStream = class_379.method_1216(dimFolder, chunk.field_962, chunk.field_963);
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
        WorldProperties levelProperties = world.method_262();
        levelProperties.setSizeOnDisk(levelProperties.getSizeOnDisk() + (long) class_379.method_1214(dimFolder, chunk.field_962, chunk.field_963));
    }

    @Override
    public void method_814(World arg, class_43 arg2) {}

    @Override
    public void method_810() {}

    @Override
    public void method_813() {}
}
