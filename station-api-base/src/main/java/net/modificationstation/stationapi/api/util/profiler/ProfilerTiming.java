package net.modificationstation.stationapi.api.util.profiler;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public final class ProfilerTiming implements Comparable<ProfilerTiming> {
    public final double parentSectionUsagePercentage;
    public final double totalUsagePercentage;
    public final long visitCount;
    public final String name;

    public ProfilerTiming(String name, double parentUsagePercentage, double totalUsagePercentage, long visitCount) {
        this.name = name;
        this.parentSectionUsagePercentage = parentUsagePercentage;
        this.totalUsagePercentage = totalUsagePercentage;
        this.visitCount = visitCount;
    }

    public int compareTo(ProfilerTiming profilerTiming) {
        if (profilerTiming.parentSectionUsagePercentage < this.parentSectionUsagePercentage) {
            return -1;
        } else {
            return profilerTiming.parentSectionUsagePercentage > this.parentSectionUsagePercentage ? 1 : profilerTiming.name.compareTo(this.name);
        }
    }

    @Environment(EnvType.CLIENT)
    public int getColor() {
        return (this.name.hashCode() & 11184810) + 4473924;
    }
}
