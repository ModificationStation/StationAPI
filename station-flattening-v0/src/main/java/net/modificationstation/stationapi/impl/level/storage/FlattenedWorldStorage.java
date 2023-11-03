package net.modificationstation.stationapi.impl.level.storage;

import com.mojang.datafixers.DSL;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_157;
import net.minecraft.class_353;
import net.minecraft.class_379;
import net.minecraft.class_591;
import net.minecraft.class_62;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.dimension.DimensionData;
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

public class FlattenedWorldStorage extends class_157 {

    public FlattenedWorldStorage(File file) {
        super(file);
    }

    @Environment(value=EnvType.CLIENT)
    @Override
    public String method_1001() {
        return "Modded " + super.method_1001();
    }

    @Environment(value=EnvType.CLIENT)
    public String getPreviousWorldFormat() {
        return super.method_1001();
    }

    @Environment(value=EnvType.CLIENT)
    public List<class_591> method_1002() {
        ArrayList<class_591> worlds = new ArrayList<>();
        for (File worldPath : Objects.requireNonNull(this.field_1706.listFiles())) {
            String worldFolder;
            WorldProperties data;
            if (!worldPath.isDirectory() || (data = this.method_1004(worldFolder = worldPath.getName())) == null) continue;
            NbtCompound worldTag = getWorldTag(worldFolder);
            boolean requiresUpdating = data.getVersion() != 19132 || NbtHelper.requiresUpdating(worldTag);
            String worldName = data.getName();
            if (worldName == null || MathHelper.isNullOrEmtpy(worldName)) worldName = worldFolder;
            worlds.add(new class_591(worldFolder, worldName, data.setLastPlayed(), data.getSizeOnDisk(), requiresUpdating));
        }
        return worlds;
    }

    public NbtCompound getWorldTag(String worldFolder) {
        File worldPath = new File(field_1706, worldFolder);
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
    public DimensionData method_1009(String string, boolean bl) {
        return new FlattenedDimensionFile(this.field_1706, string, bl);
    }

    @Override
    public boolean method_1007(String string) {
        if (super.method_1007(string))
            return true;
        NbtCompound worldTag = getWorldTag(string);
        return worldTag != null && NbtHelper.requiresUpdating(worldTag);
    }

    @Override
    public boolean method_1008(String worldFolder, class_62 progress) {
        return convertLevel(worldFolder, (type, compound) -> NbtHelper.addDataVersions(NbtHelper.update(type, compound)), progress);
    }

    public boolean convertLevel(String worldFolder, BiFunction<DSL.TypeReference, NbtCompound, NbtCompound> convertFunction, class_62 progress) {
        class_379.method_1212();
        LOGGER.info("Creating a backup of world \"" + worldFolder + "\"...");
        File worldFile = new File(field_1706, worldFolder);
        try {
            Util.pack(worldFile.toPath(), new File(field_1706, worldFolder + "-" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".zip").toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (super.method_1007(worldFolder)) {
            LOGGER.info("Converting to \"" + super.method_1001() + "\" first...");
            super.method_1008(worldFolder, progress);
        }
        progress.method_1794(0);
        List<class_353> regions = new ArrayList<>();
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

    private void scanDimensionDir(File dimensionFolder, List<class_353> regions) {
        File regionFolder = new File(dimensionFolder, "region");
        File[] regionFiles = regionFolder.listFiles((dir, name) -> new File(dir, name).isFile() && name.endsWith(".mcr"));
        if (regionFiles != null)
            Arrays.stream(regionFiles).map(class_353::new).forEach(regions::add);
    }

    private void convertChunks(List<class_353> regions, BiFunction<DSL.TypeReference, NbtCompound, NbtCompound> convertFunction, class_62 progress) {
        List<IntSet> existingChunks = new ArrayList<>();
        int totalChunks = 0;
        for (class_353 region : regions) {
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
            class_353 region = regions.get(i);
            IntSet chunks = existingChunks.get(i);
            IntIterator it = chunks.iterator();
            while (it.hasNext()) {
                int index = it.nextInt();
                int x = index & 0b11111;
                int z = index >> 5;
                DataInputStream stream = region.method_1159(x, z);
                if (stream != null) {
                    NbtCompound chunkTag = NbtIo.read(stream);
                    try {
                        stream.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    NbtCompound updatedChunkTag = convertFunction.apply(TypeReferences.CHUNK, chunkTag);
                    try (DataOutputStream outStream = region.method_1167(x, z)) {
                        NbtIo.write(updatedChunkTag, outStream);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                progress.method_1794(++updatedChunks * 100 / totalChunks);
            }
            region.method_1166();
        }
    }
}
