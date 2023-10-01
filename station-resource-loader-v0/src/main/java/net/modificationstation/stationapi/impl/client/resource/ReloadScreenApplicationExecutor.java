package net.modificationstation.stationapi.impl.client.resource;

import org.jetbrains.annotations.NotNull;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;

public enum ReloadScreenApplicationExecutor implements Executor {
    INSTANCE;

    private final Queue<Runnable> tasks = new ConcurrentLinkedQueue<>();

    @Override
    public void execute(@NotNull Runnable command) {
        tasks.add(command);
    }

    public Runnable poll() {
        return tasks.poll();
    }
}
