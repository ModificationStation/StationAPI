package net.modificationstation.stationapi.impl.level.storage;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.level.LevelProperties;
import net.minecraft.level.dimension.DimensionData;
import net.minecraft.level.storage.LevelMetadata;
import net.minecraft.level.storage.McRegionLevelStorage;
import net.minecraft.level.storage.RegionFile;
import net.minecraft.util.ProgressListener;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.io.NBTIO;
import net.minecraft.util.maths.MathHelper;
import net.modificationstation.stationapi.api.datafixer.TypeReferences;
import net.modificationstation.stationapi.api.nbt.NbtHelper;
import net.modificationstation.stationapi.impl.level.dimension.StationFlatteningDimensionFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class StationFlatteningWorldStorage extends McRegionLevelStorage {

    public StationFlatteningWorldStorage(File file) {
        super(file);
    }

    @Environment(value=EnvType.CLIENT)
    @Override
    public String getLevelFormat() {
        return "Modded " + super.getLevelFormat();
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
        return new StationFlatteningDimensionFile(this.path, string, bl);
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
        if (super.isOld(worldFolder)) {
            System.out.println("Converting to \"" + super.getLevelFormat() + "\" first...");
            super.convertLevel(worldFolder, progress);
        }
        progress.progressStagePercentage(0);
        List<RegionFile> regions = new ArrayList<>();
        File worldFile = new File(this.path, worldFolder);
        System.out.println("Scanning folders...");
        scanDimensionDir(worldFile, regions);
        File[] dims = worldFile.listFiles((dir, name) -> new File(dir, name).isDirectory() && name.startsWith("DIM"));
        if (dims != null)
            for (File dim : dims)
                scanDimensionDir(dim, regions);
        int n = regions.size();
        System.out.println("Total conversion count is (in regions) " + n);
        convertChunks(regions, n, progress);
        CompoundTag newWorldTag = new CompoundTag();
        CompoundTag newWorldDataTag = NbtHelper.addDataVersions(getWorldTag(worldFolder));
        System.out.println("Converting player inventory...");
        newWorldDataTag.put("Player", NbtHelper.addDataVersions(NbtHelper.update(TypeReferences.PLAYER, newWorldDataTag.getCompoundTag("Player"))));
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

    private void convertChunks(List<RegionFile> regions, int n, ProgressListener progress) {
        for (int i = 0; i < n; i++) {
            RegionFile region = regions.get(i);
            for (int chunk = 0; chunk < 1024; chunk++) {
                int x = chunk & 0b11111;
                int z = chunk >> 5;
                DataInputStream stream = region.getChunkDataInputStream(x, z);
                if (stream != null) {
                    CompoundTag chunkTag = NBTIO.readTag(stream);
                    try {
                        stream.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    CompoundTag updatedChunkTag = NbtHelper.addDataVersions(NbtHelper.update(TypeReferences.CHUNK, chunkTag));
                    try (DataOutputStream outStream = region.getChunkDataOutputStream(x, z)) {
                        NBTIO.writeTag(updatedChunkTag, outStream);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                int p = (i * 1024 + chunk) / (n * 10);
                progress.progressStagePercentage(p);
            }
        }
    }
}
