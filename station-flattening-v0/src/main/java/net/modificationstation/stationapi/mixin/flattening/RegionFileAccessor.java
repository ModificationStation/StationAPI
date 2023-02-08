package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.level.storage.RegionFile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RegionFile.class)
public interface RegionFileAccessor {

    @Accessor
    int[] getOffsets();
}
