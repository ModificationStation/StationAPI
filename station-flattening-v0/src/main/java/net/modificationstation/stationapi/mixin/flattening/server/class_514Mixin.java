package net.modificationstation.stationapi.mixin.flattening.server;

import net.minecraft.server.ChunkMap;
import net.modificationstation.stationapi.impl.server.network.ChunkSectionTracker;
import org.spongepowered.asm.mixin.*;

@Mixin(ChunkMap.TrackedChunk.class)
abstract class class_514Mixin {
    @Shadow
    @Final
    ChunkMap field_2136;

    @Shadow
    private int field_2138;
    @Shadow
    private int field_2139;

    @Unique
    private final ChunkSectionTracker[] stationapi_sectionTrackers = new ChunkSectionTracker[field_2136.getWorld().countVerticalSections()];

    /**
     * @author mine_diver
     * @reason early version
     */
    @Overwrite
    public void method_1753(int x, int y, int z) {
        int sectionIndex = field_2136.getWorld().getSectionIndex(y);
        if (stationapi_sectionTrackers[sectionIndex] == null)
            //noinspection DataFlowIssue
            stationapi_sectionTrackers[sectionIndex] = new ChunkSectionTracker(field_2136, (ChunkMap.TrackedChunk) (Object) this, field_2138, field_2139, sectionIndex);
        stationapi_sectionTrackers[sectionIndex].queueUpdate(x, y & 15, z);
    }

    /**
     * @author mine_diver
     * @reason early version
     */
    @Overwrite
    public void method_1752() {
        for (ChunkSectionTracker sectionTracker : stationapi_sectionTrackers)
            if (sectionTracker != null)
                sectionTracker.sendQueue();
    }
}
