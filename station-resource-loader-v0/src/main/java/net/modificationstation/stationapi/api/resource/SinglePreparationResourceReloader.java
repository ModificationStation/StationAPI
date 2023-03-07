package net.modificationstation.stationapi.api.resource;

import net.modificationstation.stationapi.api.util.profiler.Profiler;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * A base resource reloader implementation that prepares an object in a
 * single call (as opposed to in multiple concurrent tasks) and handles
 * the prepared object in the apply stage.
 * 
 * @param <T> the intermediate object type
 */
public abstract class SinglePreparationResourceReloader<T> implements ResourceReloader {

    @Override
    public final CompletableFuture<Void> reload(ResourceReloader.Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
        return CompletableFuture.supplyAsync(() -> this.prepare(manager, prepareProfiler), prepareExecutor).thenCompose(synchronizer::whenPrepared).thenAcceptAsync(prepared -> this.apply(prepared, manager, applyProfiler), applyExecutor);
    }

    /**
     * Prepares the intermediate object.
     * 
     * <p>This method is called in the prepare executor in a reload.
     * 
     * @return the prepared object
     * 
     * @param profiler the prepare profiler
     * @param manager the resource manager
     */
    protected abstract T prepare(ResourceManager manager, Profiler profiler);

    /**
     * Handles the prepared intermediate object.
     * 
     * <p>This method is called in the apply executor, or the game engine, in a
     * reload.
     * 
     * @param manager the resource manager
     * @param profiler the apply profiler
     * @param prepared the prepared object
     */
    protected abstract void apply(T prepared, ResourceManager manager, Profiler profiler);
}

