package net.modificationstation.stationapi.impl.level.dimension;

import net.minecraft.level.chunk.ChunkIO;
import net.minecraft.level.dimension.Dimension;
import net.minecraft.level.dimension.McRegionDimensionFile;
import net.modificationstation.stationapi.api.registry.DimensionRegistry;
import net.modificationstation.stationapi.api.world.dimension.VanillaDimensions;
import net.modificationstation.stationapi.impl.level.chunk.StationFlatteningWorldChunkLoader;

import java.io.File;

public class StationFlatteningDimensionFile extends McRegionDimensionFile {

    public StationFlatteningDimensionFile(File file, String string, boolean bl) {
        super(file, string, bl);
    }

    @Override
    public ChunkIO getChunkIO(Dimension dimension) {
        File worldFolder = this.getParentFolder();
        if (!DimensionRegistry.INSTANCE.getIdByLegacyId(dimension.id).map(VanillaDimensions.OVERWORLD::equals).orElse(true)) {
            File dimFolder = new File(worldFolder, "DIM" + dimension.id);
            //noinspection ResultOfMethodCallIgnored
            dimFolder.mkdirs();
            return new StationFlatteningWorldChunkLoader(dimFolder);
        } else {
            return new StationFlatteningWorldChunkLoader(worldFolder);
        }
    }
}
