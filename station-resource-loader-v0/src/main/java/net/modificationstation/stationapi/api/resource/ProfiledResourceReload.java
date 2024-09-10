package net.modificationstation.stationapi.api.resource;

import com.google.common.base.Stopwatch;
import lombok.val;
import net.modificationstation.stationapi.api.util.Unit;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.api.util.profiler.ProfileResult;
import net.modificationstation.stationapi.api.util.profiler.Profiler;
import net.modificationstation.stationapi.api.util.profiler.ProfilerSystem;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;
import static net.modificationstation.stationapi.api.util.profiler.Profiler.union;

public class ProfiledResourceReload extends SimpleResourceReload<ProfiledResourceReload.Summary> {
    private static final String LOCATION_FORMAT = "%s: %s (%s)";

    private final Stopwatch reloadTimer = Stopwatch.createUnstarted();

    @FunctionalInterface
    public interface ProfilerListener {
        void push(ResourceReloader reloader, String prefix, String location);
    }

    public ProfiledResourceReload(
            ResourceManager manager,
            List<ResourceReloader> reloaders,
            Executor prepareExecutor,
            Executor applyExecutor,
            ProfilerListener profilerListener,
            CompletableFuture<Unit> initialStage
    ) {
        super(prepareExecutor, applyExecutor, manager, reloaders, (synchronizer, resourceManager, reloader, prepare, apply) -> {
            final AtomicLong
                    prepareTimeNs = new AtomicLong(),
                    applyTimeNs = new AtomicLong();
            val managerName = resourceManager
                    .getResourceType()
                    .map(ResourceType::getDirectory)
                    .map(StringUtils::capitalize)
                    .orElseGet(() -> resourceManager.getClass().getSimpleName());
            val prepareProfiler = new ProfilerSystem(Util.nanoTimeSupplier, () -> 0, false);
            val applyProfiler = new ProfilerSystem(Util.nanoTimeSupplier, () -> 0, false);
            final BiConsumer<String, String> reloaderListener = (prefix, location) -> profilerListener.push(reloader, prefix, location);
            final Consumer<String> preparationListener = location -> reloaderListener.accept(LOCATION_FORMAT.formatted(managerName, "%s", "preparation"), location);
            final Consumer<String> applicationListener = location -> reloaderListener.accept(LOCATION_FORMAT.formatted(managerName, "%s", "application"), location);
            return reloader.reload(
                    synchronizer,
                    resourceManager,
                    union(prepareProfiler, (ListenableProfiler) location -> preparationListener.accept(prepareProfiler.getFullPath())),
                    union(applyProfiler, (ListenableProfiler) location -> applicationListener.accept(applyProfiler.getFullPath())),
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

    private interface ListenableProfiler extends Profiler {
        @Override
        default void startTick() {
            push("root");
        }

        @Override
        default void endTick() {
        }

        @Override
        default void push(Supplier<String> locationGetter) {
            push(locationGetter.get());
        }

        @Override
        default void pop() {
        }

        @Override
        default void swap(String location) {
            push(location);
        }

        @Override
        default void swap(Supplier<String> locationGetter) {
            push(locationGetter);
        }

        @Override
        default void visit(String marker) {
        }

        @Override
        default void visit(Supplier<String> markerGetter) {
        }
    }
}
