package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.class_353;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(class_353.class)
public interface RegionFileAccessor {

    @Accessor
    int[] getOffsets();
}
