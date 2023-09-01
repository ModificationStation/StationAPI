package net.modificationstation.stationapi.api.registry;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.modificationstation.stationapi.api.util.exception.MissingModException;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public final class ModID implements Comparable<@NotNull ModID> {
    private static final boolean CHECK_MISSING_MODS = false;

    @NotNull
    private static final Cache<@NotNull String, @NotNull ModID> CACHE = Caffeine.newBuilder().softValues().build();
    @NotNull
    private static final Function<@NotNull String, @NotNull ModID> NAMESPACE_FACTORY = ModID::new;

    @NotNull
    public static final ModID MINECRAFT = of("minecraft");

    public static @NotNull ModID of(@NotNull final ModContainer modContainer) {
        return of(modContainer.getMetadata());
    }

    public static @NotNull ModID of(@NotNull final ModMetadata modMetadata) {
        return of(modMetadata.getId());
    }

    public static @NotNull ModID of(@NotNull final String namespace) {
        return CACHE.get(namespace, NAMESPACE_FACTORY);
    }

    @NotNull
    private final String namespace;
    private final int hashCode;

    private ModID(@NotNull final String namespace) {
        if (CHECK_MISSING_MODS && !FabricLoader.getInstance().isModLoaded(namespace))
            throw new MissingModException(namespace);
        this.namespace = namespace;
        hashCode = toString().hashCode();
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

    @Override
    public boolean equals(@NotNull final Object other) {
        if (this == other) return true;
        if (other instanceof @NotNull final ModID otherNamespace) {
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
    public int compareTo(@NotNull final ModID o) {
        return namespace.compareTo(o.namespace);
    }
}
