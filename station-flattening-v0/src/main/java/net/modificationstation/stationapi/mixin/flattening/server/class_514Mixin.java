package net.modificationstation.stationapi.mixin.flattening.server;

import net.minecraft.class_167;
import net.modificationstation.stationapi.impl.server.network.ChunkSectionTracker;
import org.spongepowered.asm.mixin.*;

@Mixin(class_167.class_514.class)
abstract class class_514Mixin {
    @Shadow
    @Final
    class_167 field_2136;

    @Shadow
    private int field_2138;
    @Shadow
    private int field_2139;

    @Unique
    private final ChunkSectionTracker[] stationapi_sectionTrackers = new ChunkSectionTracker[field_2136.method_1741().countVerticalSections()];

    /**
     * @author mine_diver
     * @reason early version
     */
    @Overwrite
    public void method_1753(int x, int y, int z) {
        int sectionIndex = field_2136.method_1741().getSectionIndex(y);
        if (stationapi_sectionTrackers[sectionIndex] == null)
            //noinspection DataFlowIssue
            stationapi_sectionTrackers[sectionIndex] = new ChunkSectionTracker(field_2136, (class_167.class_514) (Object) this, field_2138, field_2139, sectionIndex);
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
