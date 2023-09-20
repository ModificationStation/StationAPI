package net.modificationstation.stationapi.api.nbt;

import com.mojang.datafixers.DSL;
import com.mojang.serialization.Dynamic;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.datafixer.DataFixers;
import net.modificationstation.stationapi.api.registry.ModID;
import org.apache.commons.lang3.tuple.Triple;

import java.util.List;

public class NbtHelper {

    /**
     * Uses the data fixer to update an NBT compound object.
     *
     * @param fixType the fix type
     * @param compound the NBT compound object to fix
     */
    public static CompoundTag update(DSL.TypeReference fixType, CompoundTag compound) {
        return (CompoundTag) DataFixers.update(fixType, new Dynamic<>(NbtOps.INSTANCE, compound)).getValue();
    }

    public static CompoundTag getDataVersions(CompoundTag compound) {
        return (CompoundTag) DataFixers.getDataVersions(new Dynamic<>(NbtOps.INSTANCE, compound)).getValue();
    }

    public static CompoundTag addDataVersions(CompoundTag compound) {
        return (CompoundTag) DataFixers.addDataVersions(new Dynamic<>(NbtOps.INSTANCE, compound)).getValue();
    }

    public static boolean requiresUpdating(CompoundTag compound) {
        return DataFixers.requiresUpdating(new Dynamic<>(NbtOps.INSTANCE, compound));
    }

    public static List<Triple<ModID, Integer, Integer>> getUpdateList(CompoundTag compound) {
        return DataFixers.getUpdateList(new Dynamic<>(NbtOps.INSTANCE, compound));
    }
}
