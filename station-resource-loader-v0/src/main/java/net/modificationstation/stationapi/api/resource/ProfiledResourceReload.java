package net.modificationstation.stationapi.api.resource;

import com.google.common.base.Stopwatch;
import net.modificationstation.stationapi.api.util.Unit;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.api.util.profiler.ProfileResult;
import net.modificationstation.stationapi.api.util.profiler.ProfilerSystem;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

/**
 * An implementation of resource reload that includes an additional profiling
 * summary for each reloader.
 */
public class ProfiledResourceReload
extends SimpleResourceReload<ProfiledResourceReload.Summary> {
    private final Stopwatch reloadTimer = Stopwatch.createUnstarted();

    public ProfiledResourceReload(ResourceManager manager, List<ResourceReloader> reloaders, Executor prepareExecutor, Executor applyExecutor, CompletableFuture<Unit> initialStage) {
        super(prepareExecutor, applyExecutor, manager, reloaders, (synchronizer, resourceManager, reloader, prepare, apply) -> {
            AtomicLong atomicLong = new AtomicLong();
            AtomicLong atomicLong2 = new AtomicLong();
            ProfilerSystem profilerSystem = new ProfilerSystem(Util.nanoTimeSupplier, () -> 0, false);
            ProfilerSystem profilerSystem2 = new ProfilerSystem(Util.nanoTimeSupplier, () -> 0, false);
            CompletableFuture<Void> completableFuture = reloader.reload(synchronizer, resourceManager, profilerSystem, profilerSystem2, preparation -> prepare.execute(() -> {
                long l = Util.getMeasuringTimeNano();
                preparation.run();
                atomicLong.addAndGet(Util.getMeasuringTimeNano() - l);
            }), application -> apply.execute(() -> {
                long l = Util.getMeasuringTimeNano();
                application.run();
                atomicLong2.addAndGet(Util.getMeasuringTimeNano() - l);
            }));
            return completableFuture.thenApplyAsync(dummy -> {
                LOGGER.debug("Finished reloading " + reloader.getName());
                return new Summary(reloader.getName(), profilerSystem.getResult(), profilerSystem2.getResult(), atomicLong, atomicLong2);
            }, applyExecutor);
        }, initialStage);
        this.reloadTimer.start();
        this.applyStageFuture = this.applyStageFuture.thenApplyAsync(this::finish, applyExecutor);
    }

    private List<Summary> finish(List<Summary> summaries) {
        this.reloadTimer.stop();
        long l = 0L;
        LOGGER.info("Resource reload finished after {} ms", this.reloadTimer.elapsed(TimeUnit.MILLISECONDS));
        for (Summary summary : summaries) {
            long m = TimeUnit.NANOSECONDS.toMillis(summary.prepareTimeMs.get());
            long n = TimeUnit.NANOSECONDS.toMillis(summary.applyTimeMs.get());
            long o = m + n;
            String string = summary.name;
            LOGGER.info("{} took approximately {} ms ({} ms preparing, {} ms applying)", string, o, m, n);
            l += n;
        }
        LOGGER.info("Total blocking time: {} ms", l);
        return summaries;
    }

    public static class Summary {
        final String name;
        final ProfileResult prepareProfile;
        final ProfileResult applyProfile;
        final AtomicLong prepareTimeMs;
        final AtomicLong applyTimeMs;

        Summary(String name, ProfileResult prepareProfile, ProfileResult applyProfile, AtomicLong prepareTimeMs, AtomicLong applyTimeMs) {
            this.name = name;
            this.prepareProfile = prepareProfile;
            this.applyProfile = applyProfile;
            this.prepareTimeMs = prepareTimeMs;
            this.applyTimeMs = applyTimeMs;
        }
    }
}

