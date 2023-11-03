package net.modificationstation.stationapi.mixin.flattening.client;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_43;
import net.minecraft.class_455;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.network.ModdedPacketHandler;
import net.modificationstation.stationapi.impl.level.chunk.FlattenedChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(class_455.class)
public class MixinMultiplayerChunkCache {

    @Shadow private Map<ChunkPos, class_43> multiplayerChunkCache;

    @Shadow private World level;

    @Inject(
            method = "loadChunk",
            at = @At("HEAD"),
            cancellable = true
    )
    public void loadChunk(int i, int j, CallbackInfoReturnable<class_43> cir) {
        //noinspection deprecation
        if (!((ModdedPacketHandler) ((Minecraft) FabricLoader.getInstance().getGameInstance()).getNetworkHandler()).isModded())
            return;
        ChunkPos vec2i = new ChunkPos(i, j);
        FlattenedChunk chunk = new FlattenedChunk(this.level, i, j);
        this.multiplayerChunkCache.put(vec2i, chunk);
        chunk.field_955 = true;
        cir.setReturnValue(chunk);
    }
}
