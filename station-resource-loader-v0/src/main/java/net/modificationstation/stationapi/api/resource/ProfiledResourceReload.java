package net.modificationstation.stationapi.api.resource;

import com.google.common.base.Stopwatch;
import net.modificationstation.stationapi.api.util.Unit;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.api.util.profiler.ProfileResult;
import net.modificationstation.stationapi.api.util.profiler.ReadableProfiler;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

public class ProfiledResourceReload extends SimpleResourceReload<ProfiledResourceReload.Summary> {

    private final Stopwatch reloadTimer = Stopwatch.createUnstarted();

    public ProfiledResourceReload(
            ResourceManager manager,
            List<ResourceReloader> reloaders,
            Executor prepareExecutor,
            Executor applyExecutor,
            ResourceReloaderProfilers.Factory profilersFactory,
            CompletableFuture<Unit> initialStage
    ) {
        super(prepareExecutor, applyExecutor, manager, reloaders, (synchronizer, resourceManager, reloader, prepare, apply) -> {
            final AtomicLong
                    prepareTimeNs = new AtomicLong(),
                    applyTimeNs = new AtomicLong();
            final ReadableProfiler
                    prepareProfiler,
                    applyProfiler;
            { // deconstruction
                final ResourceReloaderProfilers profilers = profilersFactory.of(reloader);
                prepareProfiler = profilers.prepare();
                applyProfiler = profilers.apply();
            }
            return reloader.reload(
                    synchronizer,
                    resourceManager,
                    prepareProfiler,
                    applyProfiler,
                    preparation -> prepare.execute(() -> {
                        long prepareStart = Util.getMeasuringTimeNano();
                        preparation.run();
                        prepareTimeNs.addAndGet(Util.getMeasuringTimeNano() - prepareStart);
                    }),
                    application -> apply.execute(() -> {
                        long applyStart = Util.getMeasuringTimeNano();
                        application.run();
                        applyTimeNs.addAndGet(Util.getMeasuringTimeNano() - applyStart);
                    })
            ).thenApplyAsync(dummy -> {
                LOGGER.debug("Finished reloading " + reloader.getName());
                return new Summary(reloader.getName(), prepareProfiler.getResult(), applyProfiler.getResult(), prepareTimeNs, applyTimeNs);
            }, applyExecutor);
        }, initialStage);
        this.reloadTimer.start();
        this.applyStageFuture = this.applyStageFuture.thenApplyAsync(this::finish, applyExecutor);
    }

    private List<Summary> finish(List<Summary> summaries) {
        this.reloadTimer.stop();
        long totalBlockingTime = 0L;
        LOGGER.info("Resource reload finished after {} ms", this.reloadTimer.elapsed(TimeUnit.MILLISECONDS));
        for (Summary summary : summaries) {
            long prepareTimeMs = TimeUnit.NANOSECONDS.toMillis(summary.prepareTimeNs.get());
            long applyTimeMs = TimeUnit.NANOSECONDS.toMillis(summary.applyTimeNs.get());
            LOGGER.info(
                    "{} took approximately {} ms ({} ms preparing, {} ms applying)",
                    summary.name, prepareTimeMs + applyTimeMs, prepareTimeMs, applyTimeMs
            );
            totalBlockingTime += applyTimeMs;
        }
        LOGGER.info("Total blocking time: {} ms", totalBlockingTime);
        return summaries;
    }

    public static class Summary {
        final String name;
        final ProfileResult
                prepareProfile,
                applyProfile;
        final AtomicLong
                prepareTimeNs,
                applyTimeNs;

        Summary(
                String name,
                ProfileResult prepareProfile,
                ProfileResult applyProfile,
                AtomicLong prepareTimeNs,
                AtomicLong applyTimeNs
        ) {
            this.name = name;
            this.prepareProfile = prepareProfile;
            this.applyProfile = applyProfile;
            this.prepareTimeNs = prepareTimeNs;
            this.applyTimeNs = applyTimeNs;
        }
    }
}
