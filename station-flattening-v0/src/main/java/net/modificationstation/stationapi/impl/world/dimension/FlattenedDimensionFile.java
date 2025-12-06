package net.modificationstation.stationapi.impl.world.dimension;

import net.minecraft.world.chunk.storage.ChunkStorage;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.storage.RegionWorldStorage;
import net.modificationstation.stationapi.api.registry.DimensionRegistry;
import net.modificationstation.stationapi.api.world.dimension.VanillaDimensions;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedWorldChunkLoader;

import java.io.File;

public class FlattenedDimensionFile extends RegionWorldStorage {

    public FlattenedDimensionFile(File file, String string, boolean bl) {
        super(file, string, bl);
    }

    @Override
    public ChunkStorage getChunkStorage(Dimension dimension) {
        File worldFolder = this.getDirectory();
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
