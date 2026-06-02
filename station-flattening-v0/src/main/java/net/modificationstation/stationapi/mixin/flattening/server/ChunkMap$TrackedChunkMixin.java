package net.modificationstation.stationapi.mixin.flattening.server;

import net.minecraft.server.ChunkMap;
import net.modificationstation.stationapi.impl.server.network.ChunkSectionTracker;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkMap.TrackedChunk.class)
abstract class ChunkMap$TrackedChunkMixin {
    @Shadow
    private int chunkX;
    @Shadow
    private int chunkZ;

    @Unique
    private ChunkMap stationapi_chunkMap;

    @Unique
    private ChunkSectionTracker[] stationapi_sectionTrackers;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void stationapi_init(ChunkMap chunkMap, int chunkX, int chunkZ, CallbackInfo ci) {
        this.stationapi_chunkMap = chunkMap;
        this.stationapi_sectionTrackers = new ChunkSectionTracker[chunkMap.getWorld().countVerticalSections()];
    }

    /**
     * @author mine_diver
     * @reason early version
     */
    @Overwrite
    public void updatePlayerChunks(int x, int y, int z) {
        int sectionIndex = stationapi_chunkMap.getWorld().getSectionIndex(y);
        if (stationapi_sectionTrackers[sectionIndex] == null)
            //noinspection DataFlowIssue
            stationapi_sectionTrackers[sectionIndex] = new ChunkSectionTracker(stationapi_chunkMap, (ChunkMap.TrackedChunk) (Object) this, chunkX, chunkZ, sectionIndex);
        stationapi_sectionTrackers[sectionIndex].queueUpdate(x, y & 15, z);
    }

    /**
     * @author mine_diver
     * @reason early version
     */
    @Overwrite
    public void updateChunk() {
        for (ChunkSectionTracker sectionTracker : stationapi_sectionTrackers)
            if (sectionTracker != null)
                sectionTracker.sendQueue();
    }
}
