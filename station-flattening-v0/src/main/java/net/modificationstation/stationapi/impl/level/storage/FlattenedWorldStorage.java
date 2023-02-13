package net.modificationstation.stationapi.impl.level.storage;

import com.mojang.datafixers.DSL;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.level.LevelProperties;
import net.minecraft.level.dimension.DimensionData;
import net.minecraft.level.storage.LevelMetadata;
import net.minecraft.level.storage.McRegionLevelStorage;
import net.minecraft.level.storage.RegionFile;
import net.minecraft.level.storage.RegionLoader;
import net.minecraft.util.ProgressListener;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.io.NBTIO;
import net.minecraft.util.maths.MathHelper;
import net.modificationstation.stationapi.api.datafixer.TypeReferences;
import net.modificationstation.stationapi.api.nbt.NbtHelper;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.impl.level.dimension.FlattenedDimensionFile;
import net.modificationstation.stationapi.mixin.flattening.RegionFileAccessor;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.BiFunction;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

public class FlattenedWorldStorage extends McRegionLevelStorage {

    public FlattenedWorldStorage(File file) {
        super(file);
    }

    @Environment(value=EnvType.CLIENT)
    @Override
    public String getLevelFormat() {
        return "Modded " + super.getLevelFormat();
    }

    @Environment(value=EnvType.CLIENT)
    public String getPreviousWorldFormat() {
        return super.getLevelFormat();
    }

    @Environment(value=EnvType.CLIENT)
    public List<LevelMetadata> getMetadata() {
        ArrayList<LevelMetadata> worlds = new ArrayList<>();
        for (File worldPath : Objects.requireNonNull(this.path.listFiles())) {
            String worldFolder;
            LevelProperties data;
            if (!worldPath.isDirectory() || (data = this.getLevelData(worldFolder = worldPath.getName())) == null) continue;
            CompoundTag worldTag = getWorldTag(worldFolder);
            boolean requiresUpdating = data.getVersion() != 19132 || NbtHelper.requiresUpdating(worldTag);
            String worldName = data.getName();
            if (worldName == null || MathHelper.isStringEmpty(worldName)) worldName = worldFolder;
            worlds.add(new LevelMetadata(worldFolder, worldName, data.getLastPlayed(), data.getSizeOnDisk(), requiresUpdating));
        }
        return worlds;
    }

    public CompoundTag getWorldTag(String worldFolder) {
        File worldPath = new File(path, worldFolder);
        if (!worldPath.exists()) return null;
        File worldData = new File(worldPath, "level.dat");
        if (worldData.exists()) try {
            return NBTIO.readGzipped(new FileInputStream(worldData)).getCompoundTag("Data");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if ((worldData = new File(worldPath, "level.dat_old")).exists()) try {
            return NBTIO.readGzipped(new FileInputStream(worldData)).getCompoundTag("Data");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public DimensionData createDimensionFile(String string, boolean bl) {
        return new FlattenedDimensionFile(this.path, string, bl);
    }

    @Override
    public boolean isOld(String string) {
        if (super.isOld(string))
            return true;
        CompoundTag worldTag = getWorldTag(string);
        return worldTag != null && NbtHelper.requiresUpdating(worldTag);
    }

    @Override
    public boolean convertLevel(String worldFolder, ProgressListener progress) {
        return convertLevel(worldFolder, (type, compound) -> NbtHelper.addDataVersions(NbtHelper.update(type, compound)), progress);
    }

    public boolean convertLevel(String worldFolder, BiFunction<DSL.TypeReference, CompoundTag, CompoundTag> convertFunction, ProgressListener progress) {
        RegionLoader.clearCache();
        LOGGER.info("Creating a backup of world \"" + worldFolder + "\"...");
        File worldFile = new File(path, worldFolder);
        try {
            Util.pack(worldFile.toPath(), new File(path, worldFolder + "-" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".zip").toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (super.isOld(worldFolder)) {
            LOGGER.info("Converting to \"" + super.getLevelFormat() + "\" first...");
            super.convertLevel(worldFolder, progress);
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
        CompoundTag newWorldTag = new CompoundTag();
        CompoundTag newWorldDataTag = convertFunction.apply(TypeReferences.LEVEL, getWorldTag(worldFolder));
        LOGGER.info("Converting player inventory...");
        newWorldDataTag.put("Player", convertFunction.apply(TypeReferences.PLAYER, newWorldDataTag.getCompoundTag("Player")));
        newWorldTag.put("Data", newWorldDataTag);
        try {
            File file = new File(worldFile, "level.dat_new");
            File file2 = new File(worldFile, "level.dat_old");
            File file3 = new File(worldFile, "level.dat");
            NBTIO.writeGzipped(newWorldTag, new FileOutputStream(file));
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

    private void convertChunks(List<RegionFile> regions, BiFunction<DSL.TypeReference, CompoundTag, CompoundTag> convertFunction, ProgressListener progress) {
        List<IntSet> existingChunks = new ArrayList<>();
        int totalChunks = 0;
        for (RegionFile region : regions) {
            int[] offsets = ((RegionFileAccessor) region).getOffsets();
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
                DataInputStream stream = region.getChunkDataInputStream(x, z);
                if (stream != null) {
                    CompoundTag chunkTag = NBTIO.readTag(stream);
                    try {
                        stream.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    CompoundTag updatedChunkTag = convertFunction.apply(TypeReferences.CHUNK, chunkTag);
                    try (DataOutputStream outStream = region.getChunkDataOutputStream(x, z)) {
                        NBTIO.writeTag(updatedChunkTag, outStream);
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
