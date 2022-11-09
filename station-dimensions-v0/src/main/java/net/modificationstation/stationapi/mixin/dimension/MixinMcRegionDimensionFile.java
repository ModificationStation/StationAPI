package net.modificationstation.stationapi.mixin.dimension;

import net.minecraft.level.chunk.ChunkIO;
import net.minecraft.level.chunk.LevelChunkLoader;
import net.minecraft.level.dimension.Dimension;
import net.minecraft.level.dimension.DimensionFile;
import net.minecraft.level.dimension.McRegionDimensionFile;
import net.modificationstation.stationapi.api.registry.DimensionRegistry;
import net.modificationstation.stationapi.api.world.dimension.VanillaDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.io.File;

@Mixin(McRegionDimensionFile.class)
public class MixinMcRegionDimensionFile extends DimensionFile {

    public MixinMcRegionDimensionFile(File file, String worldName, boolean mkdirs) {
        super(file, worldName, mkdirs);
    }

    /**
     * @reason fuck off
     * @author mine_diver
     */
    @Overwrite
    public ChunkIO getChunkIO(Dimension dimension) {
        File var2 = this.getParentFolder();
        if (!DimensionRegistry.INSTANCE.getIdByLegacyId(dimension.id).map(VanillaDimensions.OVERWORLD::equals).orElse(true)) {
            File var3 = new File(var2, "DIM" + dimension.id);
            //noinspection ResultOfMethodCallIgnored
            var3.mkdirs();
            return new LevelChunkLoader(var3);
        } else {
            return new LevelChunkLoader(var2);
        }
    }
}
