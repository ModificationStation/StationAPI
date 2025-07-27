package net.modificationstation.stationapi.mixin.flattening.client;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.chunk.MultiplayerChunkCache;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.modificationstation.stationapi.api.network.ModdedPacketHandler;
import net.modificationstation.stationapi.impl.world.StationClientWorld;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(MultiplayerChunkCache.class)
class MultiplayerChunkCacheMixin {
    @Shadow private Map<ChunkPos, Chunk> field_2553;

    @Shadow private World field_2555;

    @Inject(
            method = "method_1807",
            at = @At("HEAD"),
            cancellable = true
    )
    public void stationapi_loadChunk(int i, int j, CallbackInfoReturnable<Chunk> cir) {
        if (!((StationClientWorld) field_2555).stationAPI$isModded())
            return;
        ChunkPos vec2i = new ChunkPos(i, j);
        FlattenedChunk chunk = new FlattenedChunk(this.field_2555, i, j);
        this.field_2553.put(vec2i, chunk);
        chunk.loaded = true;
        cir.setReturnValue(chunk);
    }
}
