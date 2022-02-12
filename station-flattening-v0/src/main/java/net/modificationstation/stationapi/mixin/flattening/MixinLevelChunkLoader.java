package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.level.chunk.LevelChunkLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import static net.modificationstation.stationapi.api.StationAPI.MODID;
import static net.modificationstation.stationapi.api.registry.Identifier.of;

@Mixin(LevelChunkLoader.class)
public class MixinLevelChunkLoader {

    @Unique
    private static final String SECTIONS_TAG = of(MODID, "sections").toString();

    @ModifyConstant(
            method = "getChunk(Lnet/minecraft/level/Level;II)Lnet/minecraft/level/chunk/Chunk;",
            constant = @Constant(stringValue = "Blocks")
    )
    private String getBlocksTag(String constant) {
        return SECTIONS_TAG;
    }
}
