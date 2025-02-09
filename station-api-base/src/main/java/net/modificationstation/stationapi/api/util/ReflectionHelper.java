package net.modificationstation.stationapi.api.util;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.function.Function;

public class ReflectionHelper {
    public static void setFieldsWithAnnotation(MethodHandles.Lookup lookup, Object target, Class<? extends Annotation> annotation, Object value) throws IllegalAccessException, NoSuchFieldException {
        setFieldsWithAnnotation(lookup, target.getClass(), target, annotation, value);
    }

    public static void setFieldsWithAnnotation(MethodHandles.Lookup lookup, Class<?> targetClass, Object target, Class<? extends Annotation> annotation, Object value) throws IllegalAccessException, NoSuchFieldException {
        setFieldsWithAnnotation(lookup, targetClass, target, annotation, annotation1 -> value);
    }

    public static <T extends Annotation> void setFieldsWithAnnotation(MethodHandles.Lookup lookup, Object target, Class<T> annotation, Function<T, Object> processor) throws IllegalAccessException, NoSuchFieldException {
        setFieldsWithAnnotation(lookup, target.getClass(), target, annotation, processor);
    }

    public static <T extends Annotation> void setFieldsWithAnnotation(MethodHandles.Lookup lookup, Class<?> targetClass, Object target, Class<T> annotation, Function<T, Object> processor) throws IllegalAccessException, NoSuchFieldException {
        for (Field field : getFieldsWithAnnotation(targetClass, annotation))
            if (Modifier.isStatic(field.getModifiers()))
                lookup.findStaticVarHandle(targetClass, field.getName(), field.getType()).set(processor.apply(field.getAnnotation(annotation)));
            else
                lookup.findVarHandle(targetClass, field.getName(), field.getType()).set(target, processor.apply(field.getAnnotation(annotation)));
    }

    public static Field[] getFieldsWithAnnotation(Class<?> targetClass, Class<? extends Annotation> annotationClass) {
        Field[] fields = new Field[0];
        for (Field field : targetClass.getDeclaredFields())
            if (field.getAnnotation(annotationClass) != null) {
                fields = Arrays.copyOf(fields, fields.length + 1);
                fields[fields.length - 1] = field;
            }
        return fields;
    }
}