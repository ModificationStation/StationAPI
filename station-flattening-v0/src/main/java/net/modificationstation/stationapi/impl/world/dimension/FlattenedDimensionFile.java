package net.modificationstation.stationapi.impl.world.dimension;

import net.minecraft.world.chunk.storage.ChunkStorage;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.storage.RegionWorldStorage;
import net.modificationstation.stationapi.api.world.dimension.VanillaDimensions;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedWorldChunkLoader;

import java.io.File;

public class FlattenedDimensionFile extends RegionWorldStorage {

    public FlattenedDimensionFile(File file, String string, boolean bl) {
        super(file, string, bl);
    }

    @Override
    public ChunkStorage getChunkStorage(Dimension dimension) {
        if (dimension.type().registryEntry().matchesId(VanillaDimensions.OVERWORLD))
            return new FlattenedWorldChunkLoader(getDirectory());

        File dimFolder = new File(getDirectory(), "DIM" + dimension.id);
        //noinspection ResultOfMethodCallIgnored
        dimFolder.mkdirs();
        return new FlattenedWorldChunkLoader(dimFolder);
    }
}
