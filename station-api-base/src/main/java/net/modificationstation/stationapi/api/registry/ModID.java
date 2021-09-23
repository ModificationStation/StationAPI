package net.modificationstation.stationapi.api.registry;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.*;

public final class ModID implements Comparable<ModID> {

    @NotNull
    private static final Cache<@NotNull String, @NotNull ModID> CACHE = CacheBuilder.newBuilder().softValues().build();

    @NotNull
    private final String modid;

    public static @NotNull ModID of(@NotNull ModContainer modContainer) {
        return of(modContainer.getMetadata());
    }

    public static @NotNull ModID of(@NotNull ModMetadata modMetadata) {
        return of(modMetadata.getId());
    }

    public static @NotNull ModID of(@NotNull String modid) {
        try {
            return CACHE.get(modid, () -> new ModID(modid));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private @NotNull IllegalArgumentException modNotPresent() {
        return new IllegalArgumentException("ModID " + modid + " isn't present in the runtime!");
    }

    private ModID(@NotNull String modid) {
        this.modid = modid;
        FabricLoader.getInstance().getModContainer(this.modid).orElseThrow(this::modNotPresent);
    }

    public @NotNull ModContainer getContainer() {
        return FabricLoader.getInstance().getModContainer(modid).orElseThrow(this::modNotPresent);
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
