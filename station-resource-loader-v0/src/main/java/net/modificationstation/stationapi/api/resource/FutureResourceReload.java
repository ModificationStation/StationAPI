package net.modificationstation.stationapi.api.resource;

import java.util.concurrent.CompletableFuture;

/**
 * Represents a resource reload.
 * 
 * @see SimpleResourceReload#start
 */
public interface FutureResourceReload extends ResourceReload {
    /**
     * Returns a future for the reload. The returned future is completed when
     * the reload completes.
     */
    CompletableFuture<?> whenComplete();

    /**
     * Returns if this reload has completed, either normally or abnormally.
     */
    @Override
    default boolean isComplete() {
        return this.whenComplete().isDone();
    }

    /**
     * Throws an unchecked exception from this reload, if there is any. Does
     * nothing if the reload has not completed or terminated.
     */
    @Override
    default void throwException() {
        CompletableFuture<?> completableFuture = this.whenComplete();
        if (completableFuture.isCompletedExceptionally()) completableFuture.join();
    }
}

