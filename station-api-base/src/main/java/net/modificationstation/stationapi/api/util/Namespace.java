package net.modificationstation.stationapi.api.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.base.Suppliers;
import it.unimi.dsi.fastutil.objects.Object2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.modificationstation.stationapi.api.util.exception.MissingModException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.message.ParameterizedMessageFactory;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Slf4j
public final class Namespace implements Comparable<@NotNull Namespace> {
    private static final boolean CHECK_MISSING_MODS = false;

    @NotNull
    private static final Cache<@NotNull String, @NotNull Namespace> CACHE = Caffeine.newBuilder().softValues().build();
    @NotNull
    private static final Function<@NotNull String, @NotNull Namespace> NAMESPACE_FACTORY = Namespace::new;

    private static final Reference2ReferenceMap<Class<?>, Namespace> CLASS_CACHE = new Reference2ReferenceOpenHashMap<>();

    @NotNull
    public static final Namespace MINECRAFT = of("minecraft");

    public static @NotNull Namespace of(@NotNull final ModContainer modContainer) {
        return of(modContainer.getMetadata());
    }

    public static @NotNull Namespace of(@NotNull final ModMetadata modMetadata) {
        return of(modMetadata.getId());
    }

    public static @NotNull Namespace of(@NotNull final String namespace) {
        return CACHE.get(namespace, NAMESPACE_FACTORY);
    }

    @ApiStatus.Experimental
    public static @NotNull Namespace resolve() {
        return CLASS_CACHE.computeIfAbsent(
                StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass(),
                (Class<?> caller) -> {
                    final Path callerPath;
                    try {
                        callerPath = Paths.get(caller.getProtectionDomain().getCodeSource().getLocation().toURI());
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }

                    val mods = FabricLoader.getInstance().getAllMods().stream();
                    final Stream<ModContainer> candidates;

                    // i'm so sorry
                    if (Files.isRegularFile(callerPath)) { // regular case
                        final URI callerRoot;
                        try (val fs = FileSystems.newFileSystem(callerPath)) {
                            callerRoot = fs.getPath("/").toUri();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        candidates = mods
                                .filter(modContainer -> modContainer.getRootPaths()
                                        .stream()
                                        .filter(Files::isDirectory)
                                        .map(Path::toUri)
                                        .anyMatch(callerRoot::equals)
                                );
                    } else if (Files.isDirectory(callerPath)) { // most likely a development environment
                        candidates = mods
                                .filter(modContainer -> modContainer.getRootPaths()
                                        .stream()
                                        .filter(Files::isDirectory)
                                        .anyMatch(path -> {
                                            Path pathName;
                                            val nameCount = path.getNameCount();

                                            // IntelliJ build path test
                                            if (nameCount > 0) {
                                                pathName = path.getFileName();
                                                if (pathName.toString().equals("resources"))
                                                    return path.getParent().resolve("classes").equals(callerPath);
                                            }

                                            // Gradle build path test
                                            if (nameCount > 2) {
                                                val resources = path.getParent();
                                                pathName = resources.getFileName();
                                                if (pathName.toString().equals("resources")) {
                                                    try (val walk = Files.walk(
                                                            resources.getParent().resolve("classes"), 2
                                                    )) {
                                                        if (walk.anyMatch(callerPath::equals)) return true;
                                                    } catch (IOException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                }
                                            }

                                            return false;
                                        })
                                );
                    } else candidates = Stream.empty();

                    return of(candidates
                            .findAny()
                            .orElseThrow(() -> new IllegalCallerException("""
                                    Class "%s" has attempted to resolve its namespace, \
                                    but it's either not a part of a mod, \
                                    or was loaded in an unusual way. \
                                    Class's location resolves to "%s".\
                                    """
                                    .formatted(
                                            caller.getName(),
                                            callerPath.toUri()
                                    )
                            ))
                            .getMetadata()
                            .getId()
                    );
        });
    }

    @NotNull
    private final String namespace;
    private final int hashCode;
    private final Supplier<Object2ReferenceMap<String, Logger>> loggers;

    private Namespace(@NotNull final String namespace) {
        if (CHECK_MISSING_MODS && !FabricLoader.getInstance().isModLoaded(namespace))
            throw new MissingModException(namespace);
        this.namespace = namespace;
        hashCode = toString().hashCode();
        loggers = Suppliers.memoize(Object2ReferenceOpenHashMap::new);
    }

    public @NotNull ModContainer getContainer() {
        return FabricLoader.getInstance().getModContainer(namespace).orElseThrow(() -> new MissingModException(namespace));
    }

    public @NotNull ModMetadata getMetadata() {
        return getContainer().getMetadata();
    }

    public @NotNull String getName() {
        return getMetadata().getName();
    }

    public @NotNull Version getVersion() {
        return getMetadata().getVersion();
    }

    public @NotNull Identifier id(@NotNull final String id) {
        return Identifier.of(this, id);
    }

    public @NotNull Logger getLogger() {
        return getLogger(namespace + "|Mod");
    }

    public @NotNull Logger getLogger(String name) {
        return loggers.get().computeIfAbsent(name, (String s) -> {
            val log = LogManager.getLogger(s, ParameterizedMessageFactory.INSTANCE);
            Configurator.setLevel(s, Level.INFO);
            return log;
        });
    }

    @Override
    public boolean equals(@NotNull final Object other) {
        if (this == other) return true;
        if (other instanceof @NotNull final Namespace otherNamespace) {
            if (namespace.equals(otherNamespace.namespace))
                throw new IllegalStateException(String.format("Encountered a duplicate instance of Namespace %s!", namespace));
            return false;
        }
        return namespace.equals(other);
    }

    @Override
    public @NotNull String toString() {
        return namespace;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public int compareTo(@NotNull final Namespace o) {
        return namespace.compareTo(o.namespace);
    }
}
