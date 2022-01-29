package net.modificationstation.stationapi.api.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.MoreExecutors;
import it.unimi.dsi.fastutil.Hash;
import net.fabricmc.loader.api.FabricLoader;
import net.modificationstation.stationapi.api.util.math.MathHelper;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

public class Util {

    private static final AtomicInteger NEXT_WORKER_ID = new AtomicInteger(1);
    private static final ExecutorService MAIN_WORKER_EXECUTOR = createWorker("Main");
    public static LongSupplier nanoTimeSupplier = System::nanoTime;

    private static ExecutorService createWorker(String name) {
        int i = MathHelper.clamp(Runtime.getRuntime().availableProcessors() - 1, 1, 7);
        ExecutorService executorService2;
        if (i <= 0) {
            executorService2 = MoreExecutors.newDirectExecutorService();
        } else {
            executorService2 = new ForkJoinPool(i, (forkJoinPool) -> {
                ForkJoinWorkerThread forkJoinWorkerThread = new ForkJoinWorkerThread(forkJoinPool) {
                    protected void onTermination(Throwable throwable) {
                        if (throwable != null) {
                            LOGGER.warn("{} died", this.getName(), throwable);
                        } else {
                            LOGGER.debug("{} shutdown", this.getName());
                        }

                        super.onTermination(throwable);
                    }
                };
                forkJoinWorkerThread.setName("Worker-" + name + "-" + NEXT_WORKER_ID.getAndIncrement());
                return forkJoinWorkerThread;
            }, Util::method_18347, true);
        }

        return executorService2;
    }

    private static void method_18347(Thread thread, Throwable throwable) {
        throwOrPause(throwable);
        if (throwable instanceof CompletionException) {
            throwable = throwable.getCause();
        }

//        if (throwable instanceof CrashException) {
//            System.out.println(((CrashException)throwable).getReport().asString());
//            System.exit(-1);
//        }

        LOGGER.error(String.format("Caught exception in thread %s", thread), throwable);
    }

    public static <T extends Throwable> T throwOrPause(T t) {
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            LOGGER.error("Trying to throw a fatal exception, pausing in IDE", t);

            while(true) {
                try {
                    //noinspection BusyWait
                    Thread.sleep(1000L);
                    LOGGER.error("paused");
                } catch (InterruptedException var2) {
                    return t;
                }
            }
        } else {
            return t;
        }
    }

    public static <T> T make(T object, Consumer<T> initializer) {
        initializer.accept(object);
        return object;
    }

    public static <T> T make(Supplier<T> initializer) {
        return initializer.get();
    }

    public static <T, R> ImmutableMap<T, R> createLookupBy(Function<R, T> keyMapper, R[] values) {
        return Arrays.stream(values).collect(ImmutableMap.toImmutableMap(keyMapper, Function.identity()));
    }

    public static <K> Hash.Strategy<K> identityHashStrategy() {
        //noinspection unchecked
        return (Hash.Strategy<K>) IdentityHashStrategy.INSTANCE;
    }

    public static <V> CompletableFuture<List<V>> combine(List<? extends CompletableFuture<? extends V>> futures) {
        List<V> list = Lists.newArrayListWithCapacity(futures.size());
        CompletableFuture<?>[] completableFutures = new CompletableFuture[futures.size()];
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        futures.forEach((completableFuture2) -> {
            int i = list.size();
            list.add(null);
            completableFutures[i] = completableFuture2.whenComplete((object, throwable) -> {
                if (throwable != null) {
                    completableFuture.completeExceptionally(throwable);
                } else {
                    list.set(i, object);
                }

            });
        });
        return CompletableFuture.allOf(completableFutures).applyToEither(completableFuture, (void_) -> list);
    }

    public static Executor getMainWorkerExecutor() {
        return MAIN_WORKER_EXECUTOR;
    }

    public static <T> T getRandom(T[] array, Random random) {
        return array[random.nextInt(array.length)];
    }

    public static long getMeasuringTimeNano() {
        return nanoTimeSupplier.getAsLong();
    }

    enum IdentityHashStrategy implements Hash.Strategy<Object> {
        INSTANCE;

        public int hashCode(Object object) {
            return System.identityHashCode(object);
        }

        public boolean equals(Object object, Object object2) {
            return object == object2;
        }
    }
}
