package net.modificationstation.stationapi.api.registry;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.modificationstation.stationapi.api.util.exception.MissingModException;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class ModID implements Comparable<ModID> {

    private static final boolean CHECK_MISSING_MODS = false;

    @NotNull
    private static final Cache<@NotNull String, @NotNull ModID> CACHE = Caffeine.newBuilder().softValues().build();

    @NotNull
    public static final ModID MINECRAFT = of("minecraft");

    @NotNull
    private final String modid;
    private final int hashCode;

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
        if (CHECK_MISSING_MODS && !FabricLoader.getInstance().isModLoaded(modid))
            throw new MissingModException(modid);
        this.modid = modid;
        hashCode = toString().hashCode();
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

    public @NotNull Identifier id(String id) {
        return Identifier.of(this, id);
    }

    @Override
    public boolean equals(@NotNull Object other) {
        return this == other
                || (other instanceof ModID && modid.equals(((ModID) other).modid))
                || (other instanceof String && toString().equals(other));
    }

    @Override
    public @NotNull String toString() {
        return modid;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public int compareTo(@NotNull ModID o) {
        return modid.compareTo(o.modid);
    }
}
