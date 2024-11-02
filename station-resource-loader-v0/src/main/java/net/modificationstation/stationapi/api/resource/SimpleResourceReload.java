package net.modificationstation.stationapi.api.resource;

import net.modificationstation.stationapi.api.util.Unit;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.api.util.profiler.DummyProfiler;
import net.modificationstation.stationapi.impl.resource.loader.ResourceManagerHelperImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A simple implementation of resource reload.
 * 
 * @param <S> the result type for each reloader in the reload
 */
public class SimpleResourceReload<S> implements FutureResourceReload {
    /**
     * The weight of either prepare or apply stages' progress in the total progress
     * calculation. Has value {@value}.
     */
    private static final int FIRST_PREPARE_APPLY_WEIGHT = 2;
    /**
     * The weight of either prepare or apply stages' progress in the total progress
     * calculation. Has value {@value}.
     */
    private static final int SECOND_PREPARE_APPLY_WEIGHT = 2;
    /**
     * The weight of reloaders' progress in the total progress calculation. Has value {@value}.
     */
    private static final int RELOADER_WEIGHT = 1;
    protected final CompletableFuture<Unit> prepareStageFuture = new CompletableFuture<>();
    protected CompletableFuture<List<S>> applyStageFuture;
    final Set<ResourceReloader> waitingReloaders;
    private final int reloaderCount;
    private int toApplyCount;
    private int appliedCount;
    private final AtomicInteger toPrepareCount = new AtomicInteger();
    private final AtomicInteger preparedCount = new AtomicInteger();

    /**
     * Creates a simple resource reload without additional results.
     */
    public static SimpleResourceReload<Void> create(ResourceManager manager, List<ResourceReloader> reloaders, Executor prepareExecutor, Executor applyExecutor, CompletableFuture<Unit> initialStage) {
        return new SimpleResourceReload<>(prepareExecutor, applyExecutor, manager, reloaders, (synchronizer, resourceManager, reloader, prepare, apply) -> reloader.reload(synchronizer, resourceManager, DummyProfiler.INSTANCE, DummyProfiler.INSTANCE, prepareExecutor, apply), initialStage);
    }

    protected SimpleResourceReload(Executor prepareExecutor, final Executor applyExecutor, ResourceManager manager, List<ResourceReloader> reloaders, Factory<S> factory, CompletableFuture<Unit> initialStage) {
        this.reloaderCount = reloaders.size();
        this.toPrepareCount.incrementAndGet();
        initialStage.thenRun(this.preparedCount::incrementAndGet);
        List<CompletableFuture<S>> stages = new ArrayList<>();
        CompletableFuture<?> currentStage = initialStage;
        this.waitingReloaders = new HashSet<>(reloaders);
        for (final ResourceReloader resourceReloader : reloaders) {
            final CompletableFuture<?> finalCurrentStage = currentStage;
            CompletableFuture<S> newStage = factory.create(new ResourceReloader.Synchronizer(){

                @Override
                public <T> CompletableFuture<T> whenPrepared(T preparedObject) {
                    applyExecutor.execute(() -> {
                        SimpleResourceReload.this.waitingReloaders.remove(resourceReloader);
                        if (SimpleResourceReload.this.waitingReloaders.isEmpty()) {
                            SimpleResourceReload.this.prepareStageFuture.complete(Unit.INSTANCE);
                        }
                    });
                    return SimpleResourceReload.this.prepareStageFuture.thenCombine(finalCurrentStage, (unit, object2) -> preparedObject);
                }
            }, manager, resourceReloader, preparation -> {
                this.toPrepareCount.incrementAndGet();
                prepareExecutor.execute(() -> {
                    preparation.run();
                    this.preparedCount.incrementAndGet();
                });
            }, application -> {
                ++this.toApplyCount;
                applyExecutor.execute(() -> {
                    application.run();
                    ++this.appliedCount;
                });
            });
            stages.add(newStage);
            currentStage = newStage;
        }
        this.applyStageFuture = Util.combine(stages);
    }

    @Override
    public CompletableFuture<?> whenComplete() {
        return this.applyStageFuture;
    }

    @Override
    public float getProgress() {
        int i = this.reloaderCount - this.waitingReloaders.size();
        float f = this.preparedCount.get() * FIRST_PREPARE_APPLY_WEIGHT + this.appliedCount * FIRST_PREPARE_APPLY_WEIGHT + i * RELOADER_WEIGHT;
        float g = this.toPrepareCount.get() * SECOND_PREPARE_APPLY_WEIGHT + this.toApplyCount * SECOND_PREPARE_APPLY_WEIGHT + this.reloaderCount * RELOADER_WEIGHT;
        return f / g;
    }

    /**
     * Starts a resource reload with the content from the {@code manager} supplied
     * to the {@code reloaders}.
     * 
     * @param reloaders the reloaders performing the reload
     * @param manager the resource manager, providing resources to the reloaders
     * @param applyExecutor the executor for the apply stage, synchronous with the game engine
     * @param prepareExecutor the executor for the prepare stage, often asynchronous
     * @param initialStage the initial stage, must be completed before the reloaders can prepare resources
     */
    public static ResourceReload start(
            ResourceManager manager,
            final List<ResourceReloader> reloaders,
            Executor prepareExecutor,
            Executor applyExecutor,
            CompletableFuture<Unit> initialStage
    ) {
        return SimpleResourceReload.create(
                manager,
                manager.getResourceType()
                        .map(type -> ResourceManagerHelperImpl.sort(type, reloaders))
                        .orElse(reloaders),
                prepareExecutor,
                applyExecutor,
                initialStage
        );
    }

    public static ResourceReload start(
            ResourceManager manager,
            final List<ResourceReloader> reloaders,
            Executor prepareExecutor,
            Executor applyExecutor,
            ProfiledResourceReload.ProfilerListener profilerListener,
            CompletableFuture<Unit> initialStage
    ) {
        return new ProfiledResourceReload(
                manager,
                manager.getResourceType()
                        .map(type -> ResourceManagerHelperImpl.sort(type, reloaders))
                        .orElse(reloaders),
                prepareExecutor,
                applyExecutor,
                profilerListener,
                initialStage
        );
    }

    protected interface Factory<S> {
        CompletableFuture<S> create(ResourceReloader.Synchronizer synchronizer, ResourceManager manager, ResourceReloader reloader, Executor prepareExecutor, Executor applyExecutor);
    }
}

