package net.modificationstation.stationloader.impl.common.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class ReflectionHelper {

    private static final Field modifiers;

    static {
        try {
            modifiers = Field.class.getDeclaredField("modifiers");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static Field[] getFieldsWithAnnotation(Class<?> targetClass, Class<? extends Annotation> annotationClass) {
        Field[] fields = new Field[0];
        for (Field field : targetClass.getFields())
            if (field.getAnnotation(annotationClass) != null) {
                fields = Arrays.copyOf(fields, fields.length + 1);
                fields[fields.length - 1] = field;
            }
        return fields;
    }

    public static void setFinalField(Field field, Object instance, Object value) throws IllegalAccessException {
        int mod = field.getModifiers();
        setFieldModifiers(field, mod & ~Modifier.FINAL);
        field.set(instance, value);
        setFieldModifiers(field, mod);
    }

    public static void setFieldModifiers(Field field, int fieldModifiers) throws IllegalAccessException {
        boolean modifiersInaccessible = !modifiers.isAccessible();
        if (modifiersInaccessible)
            modifiers.setAccessible(true);
        modifiers.setInt(field, fieldModifiers);
        if (modifiersInaccessible)
            modifiers.setAccessible(false);
    }
}
