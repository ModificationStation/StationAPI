package net.modificationstation.stationapi.api.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.MoreExecutors;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.Hash;
import net.fabricmc.loader.api.FabricLoader;
import net.modificationstation.stationapi.api.util.exception.CrashException;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        if (throwable instanceof CrashException crash) {
            System.out.println(crash.getReport().asString());
            System.exit(-1);
        }

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

    public static <T, R> Map<T, R> createIdentityLookupBy(Function<R, T> keyMapper, R[] values) {
        return Collections.unmodifiableMap(Arrays.stream(values).collect(Collectors.toMap(keyMapper, Function.identity(), (u, v) -> { throw new IllegalStateException(String.format("Duplicate key %s", u)); }, IdentityHashMap::new)));
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

    public static long getMeasuringTimeMs() {
        return Util.getMeasuringTimeNano() / 1000000L;
    }

    public static long getMeasuringTimeNano() {
        return nanoTimeSupplier.getAsLong();
    }

    public static OperatingSystem getOperatingSystem() {
        String string = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        if (string.contains("win")) {
            return OperatingSystem.WINDOWS;
        }
        if (string.contains("mac")) {
            return OperatingSystem.OSX;
        }
        if (string.contains("solaris")) {
            return OperatingSystem.SOLARIS;
        }
        if (string.contains("sunos")) {
            return OperatingSystem.SOLARIS;
        }
        if (string.contains("linux")) {
            return OperatingSystem.LINUX;
        }
        if (string.contains("unix")) {
            return OperatingSystem.LINUX;
        }
        return OperatingSystem.UNKNOWN;
    }

    public static Stream<String> getJVMFlags() {
        return ManagementFactory.getRuntimeMXBean().getInputArguments().stream().filter((string) -> string.startsWith("-X"));
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

    public static <T> T assertMixin() {
        throw new AssertionError("Mixin!");
    }

    public static <T> T assertImpl() {
        throw new AssertionError("This method was never supposed to be called, as it should have been overriden by a mixin. Something is very broken!");
    }

    public static <T, R> Function<T, R> memoize(final Function<T, R> function) {
        return memoize(new HashMap<>(), function);
    }

    public static <T, R> Function<T, R> memoizeIdentity(final Function<T, R> function) {
        return memoize(new IdentityHashMap<>(), function);
    }

    private static <T, R> Function<T, R> memoize(final Map<T, R> cache, final Function<T, R> function) {
        return object -> cache.computeIfAbsent(object, function);
    }

    public static <T, U, R> BiFunction<T, U, R> memoize(final BiFunction<T, U, R> biFunction) {
        final Map<Pair<T, U>, R> cache = new HashMap<>();
        return (object, object2) -> cache.computeIfAbsent(Pair.of(object, object2), pair -> biFunction.apply(pair.getFirst(), pair.getSecond()));
    }

    public enum OperatingSystem {
        LINUX("linux"),
        SOLARIS("solaris"),
        WINDOWS("windows"){

            @Override
            protected String[] getURLOpenCommand(URL url) {
                return new String[]{"rundll32", "url.dll,FileProtocolHandler", url.toString()};
            }
        }
        ,
        OSX("mac"){

            @Override
            protected String[] getURLOpenCommand(URL url) {
                return new String[]{"open", url.toString()};
            }
        }
        ,
        UNKNOWN("unknown");

        private final String name;

        OperatingSystem(String name) {
            this.name = name;
        }

        public void open(URL url) {
            try {
                Process process = AccessController.doPrivileged((PrivilegedAction<Process>) () -> {
                    try {
                        return Runtime.getRuntime().exec(this.getURLOpenCommand(url));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                //noinspection deprecation
                for (String string : IOUtils.readLines(process.getErrorStream())) {
                    LOGGER.error(string);
                }
                process.getInputStream().close();
                process.getErrorStream().close();
                process.getOutputStream().close();
            }
            catch (IOException exception) {
                LOGGER.error("Couldn't open url '{}'", url, exception);
            }
        }

        public void open(URI uri) {
            try {
                this.open(uri.toURL());
            }
            catch (MalformedURLException malformedURLException) {
                LOGGER.error("Couldn't open uri '{}'", uri, malformedURLException);
            }
        }

        public void open(File file) {
            try {
                this.open(file.toURI().toURL());
            }
            catch (MalformedURLException malformedURLException) {
                LOGGER.error("Couldn't open file '{}'", file, malformedURLException);
            }
        }

        protected String[] getURLOpenCommand(URL url) {
            String string = url.toString();
            if ("file".equals(url.getProtocol())) {
                string = string.replace("file:", "file://");
            }
            return new String[]{"xdg-open", string};
        }

        public void open(String uri) {
            try {
                this.open(new URI(uri).toURL());
            }
            catch (IllegalArgumentException | MalformedURLException | URISyntaxException exception) {
                LOGGER.error("Couldn't open uri '{}'", uri, exception);
            }
        }

        public String getName() {
            return this.name;
        }
    }
}
