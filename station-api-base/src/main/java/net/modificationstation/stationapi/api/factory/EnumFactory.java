package net.modificationstation.stationapi.api.factory;

import net.modificationstation.stationapi.api.util.UnsafeProvider;
import sun.misc.Unsafe;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.*;

public class EnumFactory {

    public static <T extends Enum<T>> T addEnum(Class<T> enumClass, String name, Consumer<T> initializer) {
        try {
            Unsafe unsafe = UnsafeProvider.theUnsafe;

            //noinspection unchecked
            T newEnum = (T) unsafe.allocateInstance(enumClass);

            try {
                //noinspection ConstantConditions
                Enum.valueOf(enumClass, null);
            } catch (NullPointerException ignored) {
            }
            //noinspection ClassGetClass
            Field enumConstantDirectoryField = enumClass.getClass().getDeclaredField("enumConstantDirectory");
            long enumConstantDirectoryOffset = unsafe.objectFieldOffset(enumConstantDirectoryField);
            //noinspection unchecked
            Map<String, T> enumConstantDirectory = (Map<String, T>) unsafe.getObjectVolatile(enumClass, enumConstantDirectoryOffset);
            enumConstantDirectory.put(name, newEnum);

            //noinspection ClassGetClass
            Field enumConstantsField = enumClass.getClass().getDeclaredField("enumConstants");
            long enumConstantsOffset = unsafe.objectFieldOffset(enumConstantsField);
            unsafe.putObject(enumClass, enumConstantsOffset, null);

            Field valuesField = null;
            try {
                valuesField = enumClass.getDeclaredField("$VALUES");
            } catch (NoSuchFieldException e) {
                int flags = Modifier.PRIVATE | Modifier.STATIC | Modifier.FINAL | 0x1000 /*SYNTHETIC*/;
                String valueType = String.format("[L%s;", enumClass.getName().replace('.', '/'));
                for (Field field : enumClass.getDeclaredFields())
                    if ((field.getModifiers() & flags) == flags &&
                            field.getType().getName().replace('.', '/').equals(valueType)) { //Apparently some JVMs return .'s and some don't..
                        valuesField = field;
                        break;
                    }
            }
            Object valuesBase = unsafe.staticFieldBase(valuesField);
            long valuesOffset = unsafe.staticFieldOffset(valuesField);
            //noinspection unchecked
            T[] values = (T[]) unsafe.getObject(valuesBase, valuesOffset);
            values = Arrays.copyOf(values, values.length + 1);
            values[values.length - 1] = newEnum;
            unsafe.putObject(valuesBase, valuesOffset, values);

            Field ordinalField = enumClass.getSuperclass().getDeclaredField("ordinal");
            long ordinalOffset = unsafe.objectFieldOffset(ordinalField);
            unsafe.putInt(newEnum, ordinalOffset, values.length - 1);

            Field nameField = enumClass.getSuperclass().getDeclaredField("name");
            long nameOffset = unsafe.objectFieldOffset(nameField);
            unsafe.putObject(newEnum, nameOffset, name);

            initializer.accept(newEnum);
            return newEnum;
        } catch (NoSuchFieldException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
