package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.level.chunk.Chunk;
import net.modificationstation.stationapi.api.world.chunk.StationFlatteningChunk;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Chunk.class)
public class MixinChunk implements StationFlatteningChunk {}
