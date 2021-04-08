package net.modificationstation.stationapi.api.registry;

import lombok.Getter;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class ModID implements Comparable<ModID> {

    private static final Map<String, ModID> VALUES = new HashMap<>();
    private final String modid;
    @Getter
    private final ModContainer container;

    private ModID(String modid, ModContainer modContainer) {
        this.modid = modid;
        this.container = modContainer;
    }

    public static @NotNull ModID of(ModContainer modContainer) {
        return VALUES.computeIfAbsent(modContainer.getMetadata().getId(), s -> new ModID(s, modContainer));
    }

    public static @NotNull ModID of(ModMetadata modMetadata) {
        return of(modMetadata.getId());
    }

    public static @NotNull ModID of(String modid) {
        return VALUES.computeIfAbsent(modid, s -> of(Objects.requireNonNull(FabricLoader.getInstance().getModContainer(s).orElse(null))));
    }

    public ModMetadata getMetadata() {
        return container.getMetadata();
    }

    public String getName() {
        return getMetadata().getName();
    }

    public Version getVersion() {
        return getMetadata().getVersion();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof String && modid.equals(obj)) || (obj instanceof ModID && modid.equals(((ModID) obj).modid));
    }

    @NotNull
    @Override
    public String toString() {
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
