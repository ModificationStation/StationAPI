package net.modificationstation.stationapi.api.util.profiler;

import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongMaps;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;

public class ProfilerSystem implements ReadableProfiler {
    private static final long TIMEOUT_NANOSECONDS = Duration.ofMillis(100L).toNanos();
    private static final Logger LOGGER = LogManager.getLogger();
    private final List<String> path = new ArrayList<>();
    private final LongList timeList = new LongArrayList();
    private final Map<String, ProfilerSystem.LocatedInfo> locationInfos = new HashMap<>();
    private final IntSupplier endTickGetter;
    private final LongSupplier timeGetter;
    private final long startTime;
    private final int startTick;
    @Getter
    private String fullPath = "";
    private boolean tickStarted;
    @Nullable
    private ProfilerSystem.LocatedInfo currentInfo;
    private final boolean checkTimeout;

    public ProfilerSystem(LongSupplier timeGetter, IntSupplier tickGetter, boolean checkTimeout) {
        this.startTime = timeGetter.getAsLong();
        this.timeGetter = timeGetter;
        this.startTick = tickGetter.getAsInt();
        this.endTickGetter = tickGetter;
        this.checkTimeout = checkTimeout;
    }

    protected String getRoot() {
        return "root";
    }

    public void startTick() {
        if (this.tickStarted) {
            LOGGER.error("Profiler tick already started - missing endTick()?");
        } else {
            this.tickStarted = true;
            this.fullPath = "";
            this.path.clear();
            this.push(getRoot());
        }
    }

    public void endTick() {
        if (!this.tickStarted) {
            LOGGER.error("Profiler tick already ended - missing startTick()?");
        } else {
            this.pop();
            this.tickStarted = false;
            if (!this.fullPath.isEmpty()) {
                LOGGER.error("Profiler tick ended before path was fully popped (remainder: '{}'). Mismatched push/pop?", () -> ProfileResult.getHumanReadableName(this.fullPath));
            }

        }
    }

    public void push(String location) {
        if (!this.tickStarted) {
            LOGGER.error("Cannot push '{}' to profiler if profiler tick hasn't started - missing startTick()?", location);
        } else {
            if (!this.fullPath.isEmpty()) {
                this.fullPath = this.fullPath + '\u001e';
            }

            this.fullPath = this.fullPath + location;
            this.path.add(this.fullPath);
            this.timeList.add(Util.getMeasuringTimeNano());
            this.currentInfo = null;
        }
    }

    public void push(java.util.function.Supplier<String> locationGetter) {
        this.push(locationGetter.get());
    }

    public void pop() {
        if (!this.tickStarted) {
            LOGGER.error("Cannot pop from profiler if profiler tick hasn't started - missing startTick()?");
        } else if (this.timeList.isEmpty()) {
            LOGGER.error("Tried to pop one too many times! Mismatched push() and pop()?");
        } else {
            long l = Util.getMeasuringTimeNano();
            long m = this.timeList.removeLong(this.timeList.size() - 1);
            this.path.remove(this.path.size() - 1);
            long n = l - m;
            ProfilerSystem.LocatedInfo locatedInfo = this.getCurrentInfo();
            locatedInfo.time = locatedInfo.time + n;
            locatedInfo.visits = locatedInfo.visits + 1L;
            if (this.checkTimeout && n > TIMEOUT_NANOSECONDS) {
                LOGGER.warn("Something's taking too long! '{}' took aprox {} ms", () -> ProfileResult.getHumanReadableName(this.fullPath), () -> (double)n / 1000000.0D);
            }

            this.fullPath = this.path.isEmpty() ? "" : this.path.get(this.path.size() - 1);
            this.currentInfo = null;
        }
    }

    public void swap(String location) {
        this.pop();
        this.push(location);
    }

    @Environment(EnvType.CLIENT)
    public void swap(java.util.function.Supplier<String> locationGetter) {
        this.pop();
        this.push(locationGetter);

    }

    private ProfilerSystem.LocatedInfo getCurrentInfo() {
        if (this.currentInfo == null) {
            this.currentInfo = this.locationInfos.computeIfAbsent(this.fullPath, (string) -> new LocatedInfo());
        }

        return this.currentInfo;
    }

    public void visit(String marker) {
        this.getCurrentInfo().counts.addTo(marker, 1L);
    }

    public void visit(java.util.function.Supplier<String> markerGetter) {
        this.getCurrentInfo().counts.addTo(markerGetter.get(), 1L);
    }

    public ProfileResult getResult() {
        return new ProfileResultImpl(this.locationInfos, this.startTime, this.startTick, this.timeGetter.getAsLong(), this.endTickGetter.getAsInt());
    }

    static class LocatedInfo implements ProfileLocationInfo {
        private long time;
        private long visits;
        private final Object2LongOpenHashMap<String> counts;

        private LocatedInfo() {
            this.counts = new Object2LongOpenHashMap<>();
        }

        public long getTotalTime() {
            return this.time;
        }

        public long getVisitCount() {
            return this.visits;
        }

        public Object2LongMap<String> getCounts() {
            return Object2LongMaps.unmodifiable(this.counts);
        }
    }
}
