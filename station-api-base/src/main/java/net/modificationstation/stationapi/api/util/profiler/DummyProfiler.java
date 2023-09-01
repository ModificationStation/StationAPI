package net.modificationstation.stationapi.api.util.profiler;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.function.Supplier;

public class DummyProfiler implements ReadableProfiler {
    public static final DummyProfiler INSTANCE = new DummyProfiler();

    private DummyProfiler() {}

    public void startTick() {}

    public void endTick() {}

    public void push(String location) {}

    public void push(Supplier<String> locationGetter) {}

    public void pop() {}

    public void swap(String location) {}

    @Environment(EnvType.CLIENT)
    public void swap(Supplier<String> locationGetter) {}

    public void visit(String marker) {}

    public void visit(Supplier<String> markerGetter) {}

    public ProfileResult getResult() {
        return EmptyProfileResult.INSTANCE;
    }
}
