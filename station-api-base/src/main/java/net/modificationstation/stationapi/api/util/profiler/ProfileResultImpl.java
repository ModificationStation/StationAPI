package net.modificationstation.stationapi.api.util.profiler;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongMaps;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.util.Util;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Map.Entry;

public class ProfileResultImpl implements ProfileResult {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final ProfileLocationInfo EMPTY_INFO = new ProfileLocationInfo() {
        public long getTotalTime() {
            return 0L;
        }

        public long getVisitCount() {
            return 0L;
        }

        public Object2LongMap<String> getCounts() {
            return Object2LongMaps.emptyMap();
        }
    };
    private static final Splitter SPLITTER = Splitter.on('\u001e');
    private static final Comparator<Entry<String, ProfileResultImpl.CounterInfo>> COMPARATOR = Entry.<String, ProfileResultImpl.CounterInfo>comparingByValue(Comparator.comparingLong((counterInfo) -> counterInfo.totalTime)).reversed();
    private final Map<String, ? extends ProfileLocationInfo> locationInfos;
    private final long startTime;
    private final int startTick;
    private final long endTime;
    private final int endTick;
    private final int tickDuration;

    public ProfileResultImpl(Map<String, ? extends ProfileLocationInfo> locationInfos, long startTime, int startTick, long endTime, int endTick) {
        this.locationInfos = locationInfos;
        this.startTime = startTime;
        this.startTick = startTick;
        this.endTime = endTime;
        this.endTick = endTick;
        this.tickDuration = endTick - startTick;
    }

    private ProfileLocationInfo getInfo(String path) {
        ProfileLocationInfo profileLocationInfo = this.locationInfos.get(path);
        return profileLocationInfo != null ? profileLocationInfo : EMPTY_INFO;
    }

    public List<ProfilerTiming> getTimings(String parentPath) {
        ProfileLocationInfo profileLocationInfo = this.getInfo("root");
        long l = profileLocationInfo.getTotalTime();
        ProfileLocationInfo profileLocationInfo2 = this.getInfo(parentPath);
        long m = profileLocationInfo2.getTotalTime();
        long n = profileLocationInfo2.getVisitCount();
        List<ProfilerTiming> list = Lists.newArrayList();
        if (!parentPath.isEmpty()) {
            parentPath = parentPath + '\u001e';
        }

        long o = 0L;

        for (String string2 : this.locationInfos.keySet()) {
            if (isSubpath(parentPath, string2)) {
                o += this.getInfo(string2).getTotalTime();
            }
        }

        float f = (float)o;
        if (o < m) {
            o = m;
        }

        if (l < o) {
            l = o;
        }

        for (String string3 : this.locationInfos.keySet()) {
            if (isSubpath(parentPath, string3)) {
                ProfileLocationInfo profileLocationInfo3 = this.getInfo(string3);
                long p = profileLocationInfo3.getTotalTime();
                double d = (double) p * 100.0D / (double) o;
                double e = (double) p * 100.0D / (double) l;
                String string4 = string3.substring(parentPath.length());
                list.add(new ProfilerTiming(string4, d, e, profileLocationInfo3.getVisitCount()));
            }
        }

        if ((float)o > f) {
            list.add(new ProfilerTiming("unspecified", (double)((float)o - f) * 100.0D / (double)o, (double)((float)o - f) * 100.0D / (double)l, n));
        }

        Collections.sort(list);
        list.add(0, new ProfilerTiming(parentPath, 100.0D, (double)o * 100.0D / (double)l, n));
        return list;
    }

    private static boolean isSubpath(String parent, String path) {
        return path.length() > parent.length() && path.startsWith(parent) && path.indexOf(30, parent.length() + 1) < 0;
    }

    private Map<String, ProfileResultImpl.CounterInfo> setupCounters() {
        Map<String, ProfileResultImpl.CounterInfo> map = Maps.newTreeMap();
        this.locationInfos.forEach((string, profileLocationInfo) -> {
            Object2LongMap<String> object2LongMap = profileLocationInfo.getCounts();
            if (!object2LongMap.isEmpty()) {
                List<String> list = SPLITTER.splitToList(string);
                object2LongMap.forEach((stringx, long_) -> map.computeIfAbsent(stringx, (string1) -> new CounterInfo()).add(list.iterator(), long_));
            }

        });
        return map;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public int getStartTick() {
        return this.startTick;
    }

    public long getEndTime() {
        return this.endTime;
    }

    public int getEndTick() {
        return this.endTick;
    }

    public boolean save(File file) {
        //noinspection ResultOfMethodCallIgnored
        file.getParentFile().mkdirs();
        OutputStreamWriter writer = null;

        try {
            writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
            writer.write(this.asString(this.getTimeSpan(), this.getTickSpan()));
            return true;
        } catch (Throwable var8) {
            LOGGER.error("Could not save profiler results to {}", file, var8);
        } finally {
            IOUtils.closeQuietly(writer);
        }

        return false;
    }

    protected String asString(long timeSpan, int tickSpan) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("---- Minecraft Profiler Results ----\n");
        stringBuilder.append("// ");
        stringBuilder.append(generateWittyComment());
        stringBuilder.append("\n\n");
        stringBuilder.append("Version: ").append(ModID.MINECRAFT.getVersion()).append('\n');
        stringBuilder.append("Time span: ").append(timeSpan / 1000000L).append(" ms\n");
        stringBuilder.append("Tick span: ").append(tickSpan).append(" ticks\n");
        stringBuilder.append("// This is approximately ").append(String.format(Locale.ROOT, "%.2f", (float)tickSpan / ((float)timeSpan / 1.0E9F))).append(" ticks per second. It should be ").append(20).append(" ticks per second\n\n");
        stringBuilder.append("--- BEGIN PROFILE DUMP ---\n\n");
        this.appendTiming(0, "root", stringBuilder);
        stringBuilder.append("--- END PROFILE DUMP ---\n\n");
        Map<String, ProfileResultImpl.CounterInfo> map = this.setupCounters();
        if (!map.isEmpty()) {
            stringBuilder.append("--- BEGIN COUNTER DUMP ---\n\n");
            this.appendCounterDump(map, stringBuilder, tickSpan);
            stringBuilder.append("--- END COUNTER DUMP ---\n\n");
        }

        return stringBuilder.toString();
    }

    private static StringBuilder indent(StringBuilder sb, int size) {
        sb.append(String.format("[%02d] ", size));

        for(int i = 0; i < size; ++i) {
            sb.append("|   ");
        }

        return sb;
    }

    private void appendTiming(int level, String name, StringBuilder sb) {
        List<ProfilerTiming> list = this.getTimings(name);
        Object2LongMap<String> object2LongMap = ObjectUtils.firstNonNull(this.locationInfos.get(name), EMPTY_INFO).getCounts();
        object2LongMap.forEach((string, long_) -> indent(sb, level).append('#').append(string).append(' ').append(long_).append('/').append(long_ / (long)this.tickDuration).append('\n'));
        if (list.size() >= 3) {
            for(int i = 1; i < list.size(); ++i) {
                ProfilerTiming profilerTiming = list.get(i);
                indent(sb, level).append(profilerTiming.name).append('(').append(profilerTiming.visitCount).append('/').append(String.format(Locale.ROOT, "%.0f", (float)profilerTiming.visitCount / (float)this.tickDuration)).append(')').append(" - ").append(String.format(Locale.ROOT, "%.2f", profilerTiming.parentSectionUsagePercentage)).append("%/").append(String.format(Locale.ROOT, "%.2f", profilerTiming.totalUsagePercentage)).append("%\n");
                if (!"unspecified".equals(profilerTiming.name)) {
                    try {
                        this.appendTiming(level + 1, name + '\u001e' + profilerTiming.name, sb);
                    } catch (Exception var9) {
                        sb.append("[[ EXCEPTION ").append(var9).append(" ]]");
                    }
                }
            }

        }
    }

    private void appendCounter(int depth, String name, ProfileResultImpl.CounterInfo info, int tickSpan, StringBuilder sb) {
        indent(sb, depth).append(name).append(" total:").append(info.selfTime).append('/').append(info.totalTime).append(" average: ").append(info.selfTime / (long)tickSpan).append('/').append(info.totalTime / (long)tickSpan).append('\n');
        info.subCounters.entrySet().stream().sorted(COMPARATOR).forEach((entry) -> this.appendCounter(depth + 1, entry.getKey(), entry.getValue(), tickSpan, sb));
    }

    private void appendCounterDump(Map<String, ProfileResultImpl.CounterInfo> counters, StringBuilder sb, int tickSpan) {
        counters.forEach((string, counterInfo) -> {
            sb.append("-- Counter: ").append(string).append(" --\n");
            this.appendCounter(0, "root", counterInfo.subCounters.get("root"), tickSpan, sb);
            sb.append("\n\n");
        });
    }

    private static String generateWittyComment() {
        String[] strings = new String[]{"Shiny numbers!", "Am I not running fast enough? :(", "I'm working as hard as I can!", "Will I ever be good enough for you? :(", "Speedy. Zoooooom!", "Hello world", "40% better than a crash report.", "Now with extra numbers", "Now with less numbers", "Now with the same numbers", "You should add flames to things, it makes them go faster!", "Do you feel the need for... optimization?", "*cracks redstone whip*", "Maybe if you treated it better then it'll have more motivation to work faster! Poor server."};

        try {
            return strings[(int)(Util.getMeasuringTimeNano() % (long)strings.length)];
        } catch (Throwable var2) {
            return "Witty comment unavailable :(";
        }
    }

    public int getTickSpan() {
        return this.tickDuration;
    }

    static class CounterInfo {
        private long selfTime;
        private long totalTime;
        private final Map<String, ProfileResultImpl.CounterInfo> subCounters;

        private CounterInfo() {
            this.subCounters = Maps.newHashMap();
        }

        public void add(Iterator<String> pathIterator, long time) {
            this.totalTime += time;
            if (!pathIterator.hasNext()) {
                this.selfTime += time;
            } else {
                this.subCounters.computeIfAbsent(pathIterator.next(), (string) -> new CounterInfo()).add(pathIterator, time);
            }

        }
    }
}
