package net.modificationstation.stationapi.api.util.profiler;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class EmptyProfileResult implements ProfileResult {
    public static final EmptyProfileResult INSTANCE = new EmptyProfileResult();

    private EmptyProfileResult() {}

    @Environment(EnvType.CLIENT)
    public List<ProfilerTiming> getTimings(String parentPath) {
        return Collections.emptyList();
    }

    public boolean save(File file) {
        return false;
    }

    public long getStartTime() {
        return 0L;
    }

    public int getStartTick() {
        return 0;
    }

    public long getEndTime() {
        return 0L;
    }

    public int getEndTick() {
        return 0;
    }
}
