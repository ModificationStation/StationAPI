package net.modificationstation.stationapi.impl.world.storage;

import com.mojang.datafixers.DSL;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.*;
import net.minecraft.client.gui.screen.LoadingDisplay;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.chunk.storage.RegionFile;
import net.minecraft.world.chunk.storage.RegionIo;
import net.minecraft.world.storage.RegionWorldStorageSource;
import net.minecraft.world.storage.WorldSaveInfo;
import net.minecraft.world.storage.WorldStorage;
import net.modificationstation.stationapi.api.datafixer.TypeReferences;
import net.modificationstation.stationapi.api.nbt.NbtHelper;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.impl.world.dimension.FlattenedDimensionFile;
import net.modificationstation.stationapi.mixin.flattening.RegionFileAccessor;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.BiFunction;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

public class FlattenedWorldStorage extends RegionWorldStorageSource {

    public FlattenedWorldStorage(File file) {
        super(file);
    }

    @Environment(value=EnvType.CLIENT)
    @Override
    public String getName() {
        return "Modded " + super.getName();
    }

    @Environment(value=EnvType.CLIENT)
    public String getPreviousWorldFormat() {
        return super.getName();
    }

    @Environment(value=EnvType.CLIENT)
    public List<WorldSaveInfo> getAll() {
        ArrayList<WorldSaveInfo> worlds = new ArrayList<>();
        for (File worldPath : Objects.requireNonNull(this.dir.listFiles())) {
            String worldFolder;
            WorldProperties data;
            if (!worldPath.isDirectory() || (data = this.method_1004(worldFolder = worldPath.getName())) == null) continue;
            NbtCompound worldTag = getWorldTag(worldFolder);
            boolean requiresUpdating = data.getVersion() != 19132 || NbtHelper.requiresUpdating(worldTag);
            String worldName = data.getName();
            if (worldName == null || MathHelper.isNullOrEmpty(worldName)) worldName = worldFolder;
            worlds.add(new WorldSaveInfo(worldFolder, worldName, data.setLastPlayed(), data.getSizeOnDisk(), requiresUpdating));
        }
        return worlds;
    }

    public NbtCompound getWorldTag(String worldFolder) {
        File worldPath = new File(dir, worldFolder);
        if (!worldPath.exists()) return null;
        File worldData = new File(worldPath, "level.dat");
        if (worldData.exists()) try {
            return NbtIo.readCompressed(new FileInputStream(worldData)).getCompound("Data");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if ((worldData = new File(worldPath, "level.dat_old")).exists()) try {
            return NbtIo.readCompressed(new FileInputStream(worldData)).getCompound("Data");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public WorldStorage method_1009(String string, boolean bl) {
        return new FlattenedDimensionFile(this.dir, string, bl);
    }

    @Override
    public boolean needsConversion(String string) {
        if (super.needsConversion(string))
            return true;
        NbtCompound worldTag = getWorldTag(string);
        return worldTag != null && NbtHelper.requiresUpdating(worldTag);
    }

    @Override
    public boolean convert(String worldFolder, LoadingDisplay progress) {
        return convertWorld(worldFolder, (type, compound) -> NbtHelper.addDataVersions(NbtHelper.update(type, compound)), progress);
    }

    public boolean convertWorld(String worldFolder, BiFunction<DSL.TypeReference, NbtCompound, NbtCompound> convertFunction, LoadingDisplay progress) {
        RegionIo.flush();
        LOGGER.info("Creating a backup of world \"" + worldFolder + "\"...");
        File worldFile = new File(dir, worldFolder);
        try {
            Util.pack(worldFile.toPath(), new File(dir, worldFolder + "-" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".zip").toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (super.needsConversion(worldFolder)) {
            LOGGER.info("Converting to \"" + super.getName() + "\" first...");
            super.convert(worldFolder, progress);
        }
        progress.progressStagePercentage(0);
        List<RegionFile> regions = new ArrayList<>();
        LOGGER.info("Scanning folders...");
        scanDimensionDir(worldFile, regions);
        File[] dims = worldFile.listFiles((dir, name) -> new File(dir, name).isDirectory() && name.startsWith("DIM"));
        if (dims != null)
            for (File dim : dims)
                scanDimensionDir(dim, regions);
        convertChunks(regions, convertFunction, progress);
        NbtCompound newWorldTag = new NbtCompound();
        NbtCompound newWorldDataTag = convertFunction.apply(TypeReferences.LEVEL, getWorldTag(worldFolder));
        LOGGER.info("Converting player inventory...");
        newWorldDataTag.put("Player", convertFunction.apply(TypeReferences.PLAYER, newWorldDataTag.getCompound("Player")));
        newWorldTag.put("Data", newWorldDataTag);
        try {
            File file = new File(worldFile, "level.dat_new");
            File file2 = new File(worldFile, "level.dat_old");
            File file3 = new File(worldFile, "level.dat");
            NbtIo.writeCompressed(newWorldTag, new FileOutputStream(file));
            if (file2.exists()) //noinspection ResultOfMethodCallIgnored
                file2.delete();
            //noinspection ResultOfMethodCallIgnored
            file3.renameTo(file2);
            if (file3.exists()) //noinspection ResultOfMethodCallIgnored
                file3.delete();
            //noinspection ResultOfMethodCallIgnored
            file.renameTo(file3);
            if (file.exists()) //noinspection ResultOfMethodCallIgnored
                file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private void scanDimensionDir(File dimensionFolder, List<RegionFile> regions) {
        File regionFolder = new File(dimensionFolder, "region");
        File[] regionFiles = regionFolder.listFiles((dir, name) -> new File(dir, name).isFile() && name.endsWith(".mcr"));
        if (regionFiles != null)
            Arrays.stream(regionFiles).map(RegionFile::new).forEach(regions::add);
    }

    private void convertChunks(List<RegionFile> regions, BiFunction<DSL.TypeReference, NbtCompound, NbtCompound> convertFunction, LoadingDisplay progress) {
        List<IntSet> existingChunks = new ArrayList<>();
        int totalChunks = 0;
        for (RegionFile region : regions) {
            int[] offsets = ((RegionFileAccessor) region).getChunkBlockInfo();
            IntSet chunks = new IntOpenHashSet(offsets.length);
            for (int i = 0; i < offsets.length; i++)
                if (offsets[i] != 0)
                    chunks.add(i);
            existingChunks.add(chunks);
            totalChunks += chunks.size();
        }
        LOGGER.info("Total conversion count is " + totalChunks);
        int updatedChunks = 0;
        for (int i = 0; i < regions.size(); i++) {
            RegionFile region = regions.get(i);
            IntSet chunks = existingChunks.get(i);
            IntIterator it = chunks.iterator();
            while (it.hasNext()) {
                int index = it.nextInt();
                int x = index & 0b11111;
                int z = index >> 5;
                DataInputStream stream = region.getChunkInputStream(x, z);
                if (stream != null) {
                    NbtCompound chunkTag = NbtIo.read(stream);
                    try {
                        stream.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    NbtCompound updatedChunkTag = convertFunction.apply(TypeReferences.CHUNK, chunkTag);
                    try (DataOutputStream outStream = region.getChunkOutputStream(x, z)) {
                        NbtIo.write(updatedChunkTag, outStream);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                progress.progressStagePercentage(++updatedChunks * 100 / totalChunks);
            }
            region.close();
        }
    }
}
