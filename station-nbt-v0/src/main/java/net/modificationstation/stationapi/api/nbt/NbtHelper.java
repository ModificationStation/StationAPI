package net.modificationstation.stationapi.api.nbt;

import com.mojang.datafixers.DSL;
import com.mojang.serialization.Dynamic;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.datafixer.DataFixers;

import java.util.Set;

public class NbtHelper {
    /**
     * Uses the data fixer to update an NBT compound object.
     *
     * @param fixType the fix type
     * @param compound the NBT compound object to fix
     */
    public static NbtCompound update(DSL.TypeReference fixType, NbtCompound compound) {
        return (NbtCompound) DataFixers.update(fixType, new Dynamic<>(NbtOps.INSTANCE, compound)).getValue();
    }

    public static NbtCompound getDataVersions(NbtCompound compound) {
        return (NbtCompound) DataFixers.getDataVersions(new Dynamic<>(NbtOps.INSTANCE, compound)).getValue();
    }

    public static NbtCompound addDataVersions(NbtCompound compound) {
        return (NbtCompound) DataFixers.addDataVersions(new Dynamic<>(NbtOps.INSTANCE, compound)).getValue();
    }

    public static boolean requiresUpdating(NbtCompound compound) {
        return DataFixers.requiresUpdating(new Dynamic<>(NbtOps.INSTANCE, compound));
    }

    public static Set<DataFixers.UpdateData> getUpdateList(NbtCompound compound) {
        return DataFixers.getUpdateList(new Dynamic<>(NbtOps.INSTANCE, compound));
    }
}
