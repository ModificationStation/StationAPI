package net.modificationstation.stationapi.mixin.flattening.client;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.level.Level;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.level.chunk.MultiplayerChunkCache;
import net.minecraft.util.maths.Vec2i;
import net.modificationstation.stationapi.api.network.ModdedPacketHandler;
import net.modificationstation.stationapi.impl.level.chunk.FlattenedChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(MultiplayerChunkCache.class)
public class MixinMultiplayerChunkCache {

    @Shadow private Map<Vec2i, Chunk> multiplayerChunkCache;

    @Shadow private Level level;

    @Inject(
            method = "loadChunk",
            at = @At("HEAD"),
            cancellable = true
    )
    public void loadChunk(int i, int j, CallbackInfoReturnable<Chunk> cir) {
        //noinspection deprecation
        if (!((ModdedPacketHandler) ((Minecraft) FabricLoader.getInstance().getGameInstance()).getNetworkHandler()).isModded())
            return;
        Vec2i vec2i = new Vec2i(i, j);
        FlattenedChunk chunk = new FlattenedChunk(this.level, i, j);
        this.multiplayerChunkCache.put(vec2i, chunk);
        chunk.field_955 = true;
        cir.setReturnValue(chunk);
    }
}
