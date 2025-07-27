package net.modificationstation.stationapi.mixin.flattening.server;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.ChunkMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ChunkMap.TrackedChunk.class)
public interface class_514Accessor {
    @Invoker
    void invokeMethod_1756(BlockEntity tileEntity);
}
