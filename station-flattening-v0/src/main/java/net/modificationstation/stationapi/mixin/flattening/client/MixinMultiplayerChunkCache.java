package net.modificationstation.stationapi.mixin.flattening.client;

import net.minecraft.level.Level;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.level.chunk.MultiplayerChunkCache;
import net.minecraft.util.maths.Vec2i;
import net.modificationstation.stationapi.impl.level.chunk.FlattenedChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(MultiplayerChunkCache.class)
public class MixinMultiplayerChunkCache {

    @Shadow private Map<Vec2i, Chunk> multiplayerChunkCache;

    @Shadow private Level level;

    /**
     * @author mine_diver
     * @reason early version
     */
    @Overwrite
    public Chunk loadChunk(int i, int j) {
        Vec2i vec2i = new Vec2i(i, j);
        FlattenedChunk chunk = new FlattenedChunk(this.level, i, j);
        this.multiplayerChunkCache.put(vec2i, chunk);
        chunk.field_955 = true;
        return chunk;
    }
}
