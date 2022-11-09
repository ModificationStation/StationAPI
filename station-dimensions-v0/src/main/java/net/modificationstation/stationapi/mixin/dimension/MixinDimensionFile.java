package net.modificationstation.stationapi.mixin.dimension;

import net.minecraft.level.LevelManager;
import net.minecraft.level.chunk.ChunkIO;
import net.minecraft.level.dimension.Dimension;
import net.minecraft.level.dimension.DimensionFile;
import net.modificationstation.stationapi.api.registry.DimensionRegistry;
import net.modificationstation.stationapi.api.world.dimension.VanillaDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.File;

@Mixin(DimensionFile.class)
public abstract class MixinDimensionFile {

    @Shadow protected abstract File getParentFolder();

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
            return new LevelManager(var3, true);
        } else {
            return new LevelManager(var2, true);
        }
    }
}
