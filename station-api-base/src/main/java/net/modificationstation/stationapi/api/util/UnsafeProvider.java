package net.modificationstation.stationapi.api.util;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class UnsafeProvider {
    @API
    public static final Unsafe theUnsafe;

    static {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            theUnsafe = (Unsafe) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
