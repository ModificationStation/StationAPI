package net.modificationstation.stationapi.impl.level.dimension;

import net.minecraft.level.chunk.ChunkIO;
import net.minecraft.level.dimension.Dimension;
import net.minecraft.level.dimension.McRegionDimensionFile;
import net.modificationstation.stationapi.api.registry.DimensionRegistry;
import net.modificationstation.stationapi.api.world.dimension.VanillaDimensions;
import net.modificationstation.stationapi.impl.level.chunk.FlattenedWorldChunkLoader;

import java.io.File;

public class FlattenedDimensionFile extends McRegionDimensionFile {

    public FlattenedDimensionFile(File file, String string, boolean bl) {
        super(file, string, bl);
    }

    @Override
    public ChunkIO getChunkIO(Dimension dimension) {
        File worldFolder = this.getParentFolder();
        if (!DimensionRegistry.INSTANCE.getIdByLegacyId(dimension.id).map(VanillaDimensions.OVERWORLD::equals).orElse(true)) {
            File dimFolder = new File(worldFolder, "DIM" + dimension.id);
            //noinspection ResultOfMethodCallIgnored
            dimFolder.mkdirs();
            return new FlattenedWorldChunkLoader(dimFolder);
        } else {
            return new FlattenedWorldChunkLoader(worldFolder);
        }
    }
}
