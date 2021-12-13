package net.modificationstation.stationapi.api.registry;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.modificationstation.stationapi.api.util.exception.MissingModException;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class ModID implements Comparable<ModID> {

    @NotNull
    private static final Cache<@NotNull String, @NotNull ModID> CACHE = Caffeine.newBuilder().softValues().build();

    @NotNull
    private final String modid;

    public static @NotNull ModID of(@NotNull ModContainer modContainer) {
        return of(modContainer.getMetadata());
    }

    public static @NotNull ModID of(@NotNull ModMetadata modMetadata) {
        return of(modMetadata.getId());
    }

    public static @NotNull ModID of(@NotNull String modid) {
        return Objects.requireNonNull(CACHE.get(modid, ModID::new));
    }

    private ModID(@NotNull String modid) {
        this.modid = modid;
        if (!FabricLoader.getInstance().isModLoaded(this.modid))
            throw new MissingModException(this.modid);
    }

    public @NotNull ModContainer getContainer() {
        return FabricLoader.getInstance().getModContainer(modid).orElseThrow(() -> new MissingModException(modid));
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

    @Override
    public boolean equals(@NotNull Object obj) {
        return (modid.equals(obj)) || (obj instanceof ModID && modid.equals(((ModID) obj).modid));
    }

    @Override
    public @NotNull String toString() {
        return modid;
    }

    @Override
    public int hashCode() {
        return modid.hashCode();
    }

    @Override
    public int compareTo(@NotNull ModID o) {
        return modid.compareTo(o.modid);
    }

}
