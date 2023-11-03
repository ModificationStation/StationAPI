package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.class_43;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(class_43.class)
public interface ChunkAccessor {

    @Invoker
    void invokeMethod_887(int i, int j);
}
