package net.modificationstation.stationapi.api.resource;

import java.util.concurrent.CompletableFuture;

/**
 * Represents a resource reload.
 * 
 * @see SimpleResourceReload#start
 */
public interface ResourceReload {
    /**
     * Returns a future for the reload. The returned future is completed when
     * the reload completes.
     */
    CompletableFuture<?> whenComplete();

    /**
     * Returns a fraction between 0 and 1 indicating the progress of this
     * reload.
     */
    float getProgress();

    /**
     * Returns if this reload has completed, either normally or abnormally.
     */
    default boolean isComplete() {
        return this.whenComplete().isDone();
    }

    /**
     * Throws an unchecked exception from this reload, if there is any. Does
     * nothing if the reload has not completed or terminated.
     */
    default void throwException() {
        CompletableFuture<?> completableFuture = this.whenComplete();
        if (completableFuture.isCompletedExceptionally()) completableFuture.join();
    }
}

