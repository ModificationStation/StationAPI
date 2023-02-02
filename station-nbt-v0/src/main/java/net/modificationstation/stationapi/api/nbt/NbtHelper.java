package net.modificationstation.stationapi.api.nbt;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.serialization.Dynamic;
import net.minecraft.util.io.AbstractTag;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.datafixer.DataFixers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Arrays;

public class NbtHelper {

    public static boolean equals(AbstractTag tag1, AbstractTag tag2) {
        ByteArrayOutputStream data1 = new ByteArrayOutputStream();
        ByteArrayOutputStream data2 = new ByteArrayOutputStream();
        AbstractTag.writeTag(tag1, new DataOutputStream(data1));
        AbstractTag.writeTag(tag2, new DataOutputStream(data2));
        return Arrays.equals(data1.toByteArray(), data2.toByteArray());
    }

    public static <T extends AbstractTag> T copy(T tag) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        AbstractTag.writeTag(tag, new DataOutputStream(byteArrayOutputStream));
        //noinspection unchecked
        return (T) AbstractTag.readTag(new DataInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray())));
    }

    /**
     * Uses the data fixer to update an NBT compound object.
     *
     * @param fixType the fix type
     * @param compound the NBT compound object to fix
     */
    public static CompoundTag update(DSL.TypeReference fixType, CompoundTag compound) {
        return (CompoundTag) DataFixers.update(fixType, new Dynamic<>(NbtOps.INSTANCE, compound)).getValue();
    }

    public static CompoundTag addDataVersions(CompoundTag compound) {
        return (CompoundTag) DataFixers.addDataVersions(new Dynamic<>(NbtOps.INSTANCE, compound)).getValue();
    }

    public static boolean requiresUpdating(CompoundTag compound) {
        return DataFixers.requiresUpdating(new Dynamic<>(NbtOps.INSTANCE, compound));
    }
}
