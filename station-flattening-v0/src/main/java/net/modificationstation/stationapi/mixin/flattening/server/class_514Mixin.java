package net.modificationstation.stationapi.mixin.flattening.server;

import net.minecraft.server.ChunkMap;
import net.modificationstation.stationapi.impl.server.network.ChunkSectionTracker;
import org.spongepowered.asm.mixin.*;

@Mixin(ChunkMap.TrackedChunk.class)
abstract class class_514Mixin {
    @Shadow
    @Final
    ChunkMap difficulty6;

    @Shadow
    private int chunkX;
    @Shadow
    private int chunkZ;

    @Unique
    private final ChunkSectionTracker[] stationapi_sectionTrackers = new ChunkSectionTracker[difficulty6.getWorld().countVerticalSections()];

    /**
     * @author mine_diver
     * @reason early version
     */
    @Overwrite
    public void updatePlayerChunks(int x, int y, int z) {
        int sectionIndex = difficulty6.getWorld().getSectionIndex(y);
        if (stationapi_sectionTrackers[sectionIndex] == null)
            //noinspection DataFlowIssue
            stationapi_sectionTrackers[sectionIndex] = new ChunkSectionTracker(difficulty6, (ChunkMap.TrackedChunk) (Object) this, chunkX, chunkZ, sectionIndex);
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
