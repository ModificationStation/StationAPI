package net.modificationstation.stationapi.impl.world.dimension;

import net.minecraft.class_243;
import net.minecraft.class_294;
import net.minecraft.world.dimension.Dimension;
import net.modificationstation.stationapi.api.registry.DimensionRegistry;
import net.modificationstation.stationapi.api.world.dimension.VanillaDimensions;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedWorldChunkLoader;

import java.io.File;

public class FlattenedDimensionFile extends class_294 {

    public FlattenedDimensionFile(File file, String string, boolean bl) {
        super(file, string, bl);
    }

    @Override
    public class_243 method_1734(Dimension dimension) {
        File worldFolder = this.method_332();
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
