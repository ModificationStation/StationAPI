package net.modificationstation.stationapi.api.nbt;

import net.minecraft.util.io.AbstractTag;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Arrays;

public class NBTHelper {

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
}
