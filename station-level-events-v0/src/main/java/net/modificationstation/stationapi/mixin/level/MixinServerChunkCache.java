package net.modificationstation.stationapi.mixin.level;

import net.minecraft.level.Level;
import net.minecraft.level.chunk.ServerChunkCache;
import net.minecraft.level.source.LevelSource;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.level.gen.LevelGenEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(ServerChunkCache.class)
public class MixinServerChunkCache {

    @Shadow private Level level;

    @Shadow private LevelSource levelSource;
//    @Shadow private Map serverChunkCache;
//    @Shadow private Set dropSet;
//
//    private final AtomicReference<Thread> chunkThread = new AtomicReference<>(null);
    private Random modRandom;

//    @Inject(method = "loadChunk", at = @At(value = "HEAD"))
//    private void threadStuff(int a, int b, CallbackInfoReturnable<Chunk> cir) {
//        int renderDistance = (64 << 3 - ((Minecraft) FabricLoader.getInstance().getGameInstance()).options.viewDistance)/16;
//        if (serverChunkCache.keySet().size() > Math.pow(renderDistance, 2)) {
//            if (chunkThread.get() == null) {
//                chunkThread.set(new Thread(() -> {
//                    for (Object vec : serverChunkCache.keySet()) {
//                        AbstractClientPlayer player = ((Minecraft) FabricLoader.getInstance().getGameInstance()).player;
//                        Chunk chunk = (Chunk) serverChunkCache.get(vec);
//                        int chunkX = chunk.x;
//                        int chunkZ = chunk.z;
//                        double chunkDistX = Math.abs(chunkX * 16) - player.x;
//                        double chunkDistZ = Math.abs(chunkZ * 16) - player.z;
//                        if (chunkDistX > (64 << 3 - ((Minecraft) FabricLoader.getInstance().getGameInstance()).options.viewDistance) || chunkDistZ > (64 << 3 - ((Minecraft) FabricLoader.getInstance().getGameInstance()).options.viewDistance)) {
//                            dropSet.add(vec);
//                        }
//                    }
//                    chunkThread.set(null);
//                }));
//            }
//            chunkThread.get().start();
//        }
//    }

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

//    @Override
//    public String toString() {
//        return "ServerChunkCache: " + this.serverChunkCache.size() + " Drop: " + this.dropSet.size() + " Max: " + (int)Math.pow((64 << 3 - ((Minecraft) FabricLoader.getInstance().getGameInstance()).options.viewDistance)/16, 2);
//    }
}
