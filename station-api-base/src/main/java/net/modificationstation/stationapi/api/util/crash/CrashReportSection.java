package net.modificationstation.stationapi.api.util.crash;

import com.google.common.collect.Lists;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CrashReportSection {
    public static String createPositionString(BlockView world, double x, double y, double z) {
        return String.format(Locale.ROOT, "%.2f,%.2f,%.2f - %s", x, y, z, CrashReportSection.createPositionString(world, new BlockPos((int) x, (int) y, (int) z)));
    }

    public static String createPositionString(BlockView world, BlockPos pos) {
        return CrashReportSection.createPositionString(world, pos.x, pos.y, pos.z);
    }

    public static String createPositionString(BlockView world, int x, int y, int z) {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            stringBuilder.append(String.format("World: (%d,%d,%d)", x, y, z));
        } catch (Throwable var16) {
            stringBuilder.append("(Error finding world loc)");
        }

        stringBuilder.append(", ");

        int r;
        int s;
        int t;
        int u;
        int v;
        int w;
        int aa;
        int ab;
        int ac;
        try {
            r = x >> 4;
            s = z >> 4;
            t = x & 15;
            u = y >> 4;
            v = z & 15;
            w = r << 4;
            aa = s << 4;
            ab = (r + 1 << 4) - 1;
            ac = (s + 1 << 4) - 1;
            stringBuilder.append(String.format("Chunk: (at %d,%d,%d in %d,%d; contains blocks %d,0,%d to %d,255,%d)", t, u, v, r, s, w, aa, ab, ac));
        } catch (Throwable var15) {
            stringBuilder.append("(Error finding chunk loc)");
        }

        stringBuilder.append(", ");

        try {
            r = x >> 9;
            s = z >> 9;
            t = r << 5;
            u = s << 5;
            v = (r + 1 << 5) - 1;
            w = (s + 1 << 5) - 1;
            aa = r << 9;
            ab = s << 9;
            ac = (r + 1 << 9) - 1;
            int ad = (s + 1 << 9) - 1;
            stringBuilder.append(String.format("Region: (%d,%d; contains chunks %d,%d to %d,%d, blocks %d,0,%d to %d,255,%d)", r, s, t, u, v, w, aa, ab, ac, ad));
        } catch (Throwable var14) {
            stringBuilder.append("(Error finding world loc)");
        }

        return stringBuilder.toString();
    }

    private final CrashReport report;
    private final String title;
    private final List<CrashReportSection.Element> elements = Lists.newArrayList();
    private StackTraceElement[] stackTrace = new StackTraceElement[0];

    public CrashReportSection(CrashReport report, String title) {
        this.report = report;
        this.title = title;
    }

    public CrashReportSection add(String string, CrashCallable<String> crashCallable) {
        try {
            this.add(string, crashCallable.call());
        } catch (Throwable var4) {
            this.add(string, var4);
        }

        return this;
    }

    public CrashReportSection add(String name, Object object) {
        this.elements.add(new CrashReportSection.Element(name, object));
        return this;
    }

    public void add(String name, Throwable throwable) {
        this.add(name, (Object)throwable);
    }

    public int initStackTrace(int ignoredCallCount) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        if (stackTraceElements.length <= 0) {
            return 0;
        } else {
            this.stackTrace = new StackTraceElement[stackTraceElements.length - 3 - ignoredCallCount];
            System.arraycopy(stackTraceElements, 3 + ignoredCallCount, this.stackTrace, 0, this.stackTrace.length);
            return this.stackTrace.length;
        }
    }

    public boolean isOperator(StackTraceElement stackTraceElement, StackTraceElement stackTraceElement2) {
        if (this.stackTrace.length != 0 && stackTraceElement != null) {
            StackTraceElement stackTraceElement3 = this.stackTrace[0];
            if (stackTraceElement3.isNativeMethod() == stackTraceElement.isNativeMethod() && stackTraceElement3.getClassName().equals(stackTraceElement.getClassName()) && Objects.equals(stackTraceElement3.getFileName(), stackTraceElement.getFileName()) && stackTraceElement3.getMethodName().equals(stackTraceElement.getMethodName())) {
                if (stackTraceElement2 == null == this.stackTrace.length > 1) {
                    return false;
                } else if (stackTraceElement2 != null && !this.stackTrace[1].equals(stackTraceElement2)) {
                    return false;
                } else {
                    this.stackTrace[0] = stackTraceElement;
                    return true;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void trimStackTraceEnd(int callCount) {
        StackTraceElement[] stackTraceElements = new StackTraceElement[this.stackTrace.length - callCount];
        System.arraycopy(this.stackTrace, 0, stackTraceElements, 0, stackTraceElements.length);
        this.stackTrace = stackTraceElements;
    }

    public void addStackTrace(StringBuilder stringBuilder) {
        stringBuilder.append("-- ").append(this.title).append(" --\n");
        stringBuilder.append("Details:");

        for (Element element : this.elements) {
            stringBuilder.append("\n\t");
            stringBuilder.append(element.getName());
            stringBuilder.append(": ");
            stringBuilder.append(element.getDetail());
        }

        if (this.stackTrace != null && this.stackTrace.length > 0) {
            stringBuilder.append("\nStacktrace:");
            StackTraceElement[] var6 = this.stackTrace;

            for (StackTraceElement stackTraceElement : var6) {
                stringBuilder.append("\n\tat ");
                stringBuilder.append(stackTraceElement);
            }
        }

    }

    public StackTraceElement[] getStackTrace() {
        return this.stackTrace;
    }

    static class Element {
        private final String name;
        private final String detail;

        public Element(String name, @Nullable Object detail) {
            this.name = name;
            if (detail == null) {
                this.detail = "~~NULL~~";
            } else if (detail instanceof Throwable throwable) {
                this.detail = "~~ERROR~~ " + throwable.getClass().getSimpleName() + ": " + throwable.getMessage();
            } else {
                this.detail = detail.toString();
            }

        }

        public String getName() {
            return this.name;
        }

        public String getDetail() {
            return this.detail;
        }
    }
}
