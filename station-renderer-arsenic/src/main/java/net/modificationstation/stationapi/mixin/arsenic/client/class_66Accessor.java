package net.modificationstation.stationapi.mixin.arsenic.client;

import net.minecraft.client.render.chunk.ChunkBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ChunkBuilder.class)
public interface class_66Accessor {

    @Invoker("method_306")
    void stationapi$method_306();
}
