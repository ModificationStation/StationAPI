package net.modificationstation.stationapi.mixin.level;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.AbstractClientPlayer;
import net.minecraft.level.Level;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.level.chunk.ChunkIO;
import net.minecraft.level.chunk.ServerChunkCache;
import net.minecraft.level.source.LevelSource;
import net.minecraft.util.maths.Vec2i;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.level.gen.LevelGenEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Mixin(ServerChunkCache.class)
public abstract class MixinServerChunkCache {

    @Shadow private Level level;

    @Shadow private LevelSource levelSource;
    @Shadow private Map serverChunkCache;

    @Shadow protected abstract void method_1050(Chunk arg);

    @Shadow protected abstract void method_1049(Chunk arg);

    @Shadow private List field_1230;
    private Random modRandom;

    @Inject(method = "decorate(Lnet/minecraft/level/source/LevelSource;II)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/level/source/LevelSource;decorate(Lnet/minecraft/level/source/LevelSource;II)V", shift = At.Shift.AFTER))
    private void onPopulate(LevelSource levelSource, int chunkX, int chunkZ, CallbackInfo ci) {
        int blockX = chunkX * 16;
        int blockZ = chunkZ * 16;
        if (modRandom == null)
            modRandom = new Random();
        modRandom.setSeed(level.getSeed());
        long xRandomMultiplier = (modRandom.nextLong() / 2L) * 2L + 1L;
        long zRandomMultiplier = (modRandom.nextLong() / 2L) * 2L + 1L;
        modRandom.setSeed((long) chunkX * xRandomMultiplier + (long) chunkZ * zRandomMultiplier ^ level.getSeed());
        StationAPI.EVENT_BUS.post(new LevelGenEvent.ChunkDecoration(level, this.levelSource, level.getBiomeSource().getBiome(blockX + 16, blockZ + 16), blockX, blockZ, modRandom));
    }

    @Inject(method = "method_1050", at = @At(value = "HEAD"))
    private void fixChunkUnloading(Chunk chunk, CallbackInfo ci) {
        int renderDistance = (64 << 3 - ((Minecraft) FabricLoader.getInstance().getGameInstance()).options.viewDistance) / 16;
        Vec2i playerPos = new Vec2i(((Minecraft) FabricLoader.getInstance().getGameInstance()).player.chunkX, ((Minecraft) FabricLoader.getInstance().getGameInstance()).player.chunkZ);
        if (Math.abs(chunk.x - playerPos.x) > renderDistance || Math.abs(chunk.z - playerPos.z) > renderDistance) {
            chunk.method_883();
        }
    }


    @Inject(method = "method_1050", at = @At(value = "TAIL"))
    private void fixChunkUnloading2(Chunk chunk, CallbackInfo ci) {
        int renderDistance = (64 << 3 - ((Minecraft) FabricLoader.getInstance().getGameInstance()).options.viewDistance)/16;
        Vec2i playerPos = new Vec2i(((Minecraft) FabricLoader.getInstance().getGameInstance()).player.chunkX, ((Minecraft) FabricLoader.getInstance().getGameInstance()).player.chunkZ);
        if (Math.abs(chunk.x - playerPos.x) > renderDistance || Math.abs(chunk.z - playerPos.z) > renderDistance) {
            method_1049(chunk);
            serverChunkCache.remove(Vec2i.hash(chunk.x, chunk.z));
            field_1230.remove(chunk);
        }
    }

}
