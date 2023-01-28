package net.modificationstation.stationapi.impl.level.storage;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.level.dimension.DimensionData;
import net.minecraft.level.storage.McRegionLevelStorage;
import net.modificationstation.stationapi.impl.level.dimension.StationFlatteningDimensionFile;

import java.io.File;

public class StationFlatteningWorldStorage extends McRegionLevelStorage {

    public static int currentVersion = 69420;

    public StationFlatteningWorldStorage(File file) {
        super(file);
//        DataFixerBuilder builder = new DataFixerBuilder(currentVersion);
//        builder.addSchema()
    }

    @Environment(value=EnvType.CLIENT)
    @Override
    public String getLevelFormat() {
        return "Station Flattening";
    }

    @Override
    public DimensionData createDimensionFile(String string, boolean bl) {
        return new StationFlatteningDimensionFile(this.path, string, bl);
    }
}
