package net.modificationstation.stationapi.api.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.MoreExecutors;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceMaps;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

public class Util {

    private static final int MAX_PARALLELISM = 255;
    private static final String MAX_BG_THREADS_PROPERTY = "max.bg.threads";
    private static final AtomicInteger NEXT_WORKER_ID = new AtomicInteger(1);
    private static final ExecutorService BOOTSTRAP_EXECUTOR = Util.createWorker("Bootstrap");
    private static final ExecutorService MAIN_WORKER_EXECUTOR = createWorker("Main");
    public static LongSupplier nanoTimeSupplier = System::nanoTime;

    private static ExecutorService createWorker(String name) {
        int i = MathHelper.clamp(Runtime.getRuntime().availableProcessors() - 1, 1, Util.getMaxBackgroundThreads());
        return i <= 0 ? MoreExecutors.newDirectExecutorService() : new ForkJoinPool(i, forkJoinPool -> {
            ForkJoinWorkerThread forkJoinWorkerThread = new ForkJoinWorkerThread(forkJoinPool){

                @Override
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
        }, Util::uncaughtExceptionHandler, true);
    }

    private static int getMaxBackgroundThreads() {
        String string = System.getProperty(MAX_BG_THREADS_PROPERTY);
        if (string != null) {
            try {
                int i = Integer.parseInt(string);
                if (i >= 1 && i <= MAX_PARALLELISM) {
                    return i;
                }
                LOGGER.error("Wrong {} property value '{}'. Should be an integer value between 1 and {}.", MAX_BG_THREADS_PROPERTY, string, MAX_PARALLELISM);
            }
            catch (NumberFormatException numberFormatException) {
                LOGGER.error("Could not parse {} property value '{}'. Should be an integer value between 1 and {}.", MAX_BG_THREADS_PROPERTY, string, MAX_PARALLELISM);
            }
        }
        return 255;
    }

    private static void uncaughtExceptionHandler(Thread thread, Throwable throwable) {
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

    public static void error(String message) {
        LOGGER.error(message);
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

    public static <T, R> Reference2ReferenceMap<T, R> createReference2ReferenceLookupBy(Function<R, T> keyMapper, R[] values, IntFunction<T[]> keyArrayFactory) {
        return Reference2ReferenceMaps.unmodifiable(new Reference2ReferenceOpenHashMap<>(Arrays.stream(values).map(keyMapper).toArray(keyArrayFactory), values));
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

    public static ExecutorService getBootstrapExecutor() {
        return BOOTSTRAP_EXECUTOR;
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

    public static <T> List<T> copyShuffled(Stream<T> stream, Random random) {
        ObjectArrayList<T> objectArrayList = stream.collect(ObjectArrayList.toList());
        Util.shuffle(objectArrayList, random);
        return objectArrayList;
    }

    public static IntArrayList shuffle(IntStream stream, Random random) {
        IntArrayList intArrayList = IntArrayList.wrap(stream.toArray());
        for (int j = intArrayList.size(); j > 1; --j) {
            int k = random.nextInt(j);
            intArrayList.set(j - 1, intArrayList.set(k, intArrayList.getInt(j - 1)));
        }
        return intArrayList;
    }

    public static <T> List<T> copyShuffled(T[] array, Random random) {
        ObjectArrayList<T> objectArrayList = new ObjectArrayList<>(array);
        Util.shuffle(objectArrayList, random);
        return objectArrayList;
    }

    public static <T> List<T> copyShuffled(ObjectArrayList<T> list, Random random) {
        ObjectArrayList<T> objectArrayList = new ObjectArrayList<>(list);
        Util.shuffle(objectArrayList, random);
        return objectArrayList;
    }

    public static <T> void shuffle(ObjectArrayList<T> list, Random random) {
        for (int j = list.size(); j > 1; --j) {
            int k = random.nextInt(j);
            list.set(j - 1, list.set(k, list.get(j - 1)));
        }
    }

    public static Consumer<String> addPrefix(String prefix, Consumer<String> consumer) {
        return string -> consumer.accept(prefix + string);
    }

    public static DataResult<int[]> toArray(IntStream stream, int length) {
        int[] is = stream.limit(length + 1).toArray();
        if (is.length != length) {
            String string = "Input is not a list of " + length + " ints";
            if (is.length >= length) {
                return DataResult.error(string, Arrays.copyOf(is, length));
            }
            return DataResult.error(string);
        }
        return DataResult.success(is);
    }

    public static <T> DataResult<List<T>> toArray(List<T> list, int length) {
        if (list.size() != length) {
            String string = "Input is not a list of " + length + " elements";
            if (list.size() >= length) {
                return DataResult.error(string, list.subList(0, length));
            }
            return DataResult.error(string);
        }
        return DataResult.success(list);
    }

    public static <T> T getRandom(List<T> list, Random random) {
        return list.get(random.nextInt(list.size()));
    }

    public static <T> Optional<T> getRandomOrEmpty(List<T> list, Random random) {
        if (list.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(Util.getRandom(list, random));
    }

    public static void pack(Path sourceDirPath, Path zipFilePath) throws IOException {
        Path p = Files.createFile(zipFilePath);
        try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p))) {
            try (Stream<Path> walk = Files.walk(sourceDirPath)) {
                Path parentPath = sourceDirPath.getParent();
                walk.forEach(path -> {
                    boolean isDir = Files.isDirectory(path);
                    String entryPath = parentPath.relativize(path).toString();
                    if (isDir)
                        entryPath += "/";
                    try {
                        zs.putNextEntry(new ZipEntry(entryPath));
                        if (!isDir)
                            Files.copy(path, zs);
                        zs.closeEntry();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
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
