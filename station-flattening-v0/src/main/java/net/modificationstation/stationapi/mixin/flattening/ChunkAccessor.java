package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Chunk.class)
public interface ChunkAccessor {
    @Invoker
    void invokeMethod_887(int i, int j);
}
