package net.modificationstation.stationapi.api.tick;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public enum TickScheduler {
    CLIENT_RENDER_START,
    CLIENT_RENDER_END;

    private final Queue<Runnable>
            distributed = new ConcurrentLinkedQueue<>(),
            immediate = new ConcurrentLinkedQueue<>();

    /**
     * Adds the command to a queue that's only polled once a tick.
     *
     * <p>Useful for distributing commands added concurrently over several ticks.
     *
     * @param command the command to perform
     */
    public void distributed(final @NotNull Runnable command) {
        distributed.add(command);
    }

    /**
     * Executes the command once and as soon as the scheduler ticks.
     *
     * @param command the command to perform
     */
    public void immediate(final @NotNull Runnable command) {
        immediate.add(command);
    }

    /**
     * Ticks the scheduler.
     *
     * <p>For internal usage only.
     */
    @ApiStatus.Internal
    public void tick() {
        // Adding the oldest distributed command
        final Runnable distributedCommand = distributed.poll();
        if (distributedCommand != null) immediate(distributedCommand);

        // Running all immediate commands
        Runnable command;
        while ((command = immediate.poll()) != null) command.run();
    }
}
