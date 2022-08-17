package net.modificationstation.stationapi.api.factory;

import sun.reflect.ReflectionFactory;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import static net.modificationstation.stationapi.api.util.UnsafeProvider.theUnsafe;

public class EnumFactory {

    private static final Function<Constructor<?>, Object> NEW_CONSTRUCTOR_ACCESSOR;
    private static final BiFunction<Object, Object[], Object> NEW_INSTANCE;

    static {
        try {
            Field implLookupField = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
            MethodHandles.Lookup implLookup = (MethodHandles.Lookup) theUnsafe.getObject(theUnsafe.staticFieldBase(implLookupField), theUnsafe.staticFieldOffset(implLookupField));
            Field rfDelegateField = ReflectionFactory.class.getDeclaredField("delegate");
            Class<?> rfClass = rfDelegateField.getType();
            rfDelegateField.setAccessible(true);
            Object reflectionFactory = rfDelegateField.get(null);
            Class<?> constructorAccessorClass = Class.forName("jdk.internal.reflect.ConstructorAccessor");
            MethodHandle newConstructorAccessorHandle = implLookup.findVirtual(rfClass, "newConstructorAccessor", MethodType.methodType(constructorAccessorClass, Constructor.class));
            MethodHandle newInstanceHandle = implLookup.findVirtual(constructorAccessorClass, "newInstance", MethodType.methodType(Object.class, Object[].class));
            NEW_CONSTRUCTOR_ACCESSOR = constructor -> {
                try {
                    return newConstructorAccessorHandle.invoke(reflectionFactory, constructor);
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            };
            NEW_INSTANCE = (constructorAccessor, params) -> {
                try {
                    return newInstanceHandle.invoke(constructorAccessor, params);
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            };
        } catch (NoSuchFieldException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private static Object getConstructorAccessor(Class<?> enumClass, Class<?>[] additionalParameterTypes) throws Exception {
        Class<?>[] parameterTypes = new Class[additionalParameterTypes.length + 2];
        parameterTypes[0] = String.class;
        parameterTypes[1] = int.class;
        System.arraycopy(additionalParameterTypes, 0, parameterTypes, 2, additionalParameterTypes.length);
        return NEW_CONSTRUCTOR_ACCESSOR.apply(enumClass.getDeclaredConstructor(parameterTypes));
    }

    private static <T extends Enum<?>> T makeEnum(Class<T> enumClass, String value, int ordinal, Class<?>[] additionalTypes, Object... additionalValues) throws Exception {
        Object[] parms = new Object[additionalValues.length + 2];
        parms[0] = value;
        parms[1] = ordinal;
        System.arraycopy(additionalValues, 0, parms, 2, additionalValues.length);
        return enumClass.cast(NEW_INSTANCE.apply(getConstructorAccessor(enumClass, additionalTypes), parms));
    }

    private static void setFailsafeFieldValue(Field field, Object target, Object value) {
        Object fieldBase;
        long fieldOffset;
        if (Modifier.isStatic(field.getModifiers())) {
            fieldBase = theUnsafe.staticFieldBase(field);
            fieldOffset = theUnsafe.staticFieldOffset(field);
        } else {
            fieldBase = target;
            fieldOffset = theUnsafe.objectFieldOffset(field);
        }
        theUnsafe.putObject(fieldBase, fieldOffset, value);
    }

    private static void blankField(Class<?> enumClass, String fieldName) {
        for (Field field : Class.class.getDeclaredFields())
            if (field.getName().contains(fieldName)) {
                setFailsafeFieldValue(field, enumClass, null);
                break;
            }
    }

    private static void cleanEnumCache(Class<?> enumClass) {
        blankField(enumClass, "enumConstantDirectory");
        blankField(enumClass, "enumConstants");
    }

    public static <T extends Enum<?>> T addEnum(Class<T> enumType, String enumName, Class<?>[] paramTypes, Object... paramValues) {
        Field valuesField = null;
        Field[] fields = enumType.getDeclaredFields();

        for (Field field : fields) {
            String name = field.getName();
            if (name.equals("$VALUES") || name.equals("ENUM$VALUES")) { //Added 'ENUM$VALUES' because Eclipse's internal compiler doesn't follow standards
                valuesField = field;
                break;
            }
        }

        int flags = Modifier.PRIVATE | Modifier.STATIC | Modifier.FINAL | 0x1000 /*SYNTHETIC*/;
        if (valuesField == null) {
            String valueType = String.format("[L%s;", enumType.getName().replace('.', '/'));

            for (Field field : fields)
                if ((field.getModifiers() & flags) == flags &&
                        field.getType().getName().replace('.', '/').equals(valueType)) { //Apparently some JVMs return .'s and some don't..
                    valuesField = field;
                    break;
                }
        }

        Objects.requireNonNull(valuesField).setAccessible(true);

        try {
            Object[] previousValues = (Object[]) valuesField.get(enumType);
            List<T> values = new ArrayList<>();
            for (Object previousValue : previousValues)
                values.add(enumType.cast(previousValue));
            T newValue = makeEnum(enumType, enumName, values.size(), paramTypes, paramValues);
            values.add(newValue);
            @SuppressWarnings("unchecked")
            T[] valuesArray = values.toArray((T[]) Array.newInstance(enumType, 0));
            setFailsafeFieldValue(valuesField, null, valuesArray);
            cleanEnumCache(enumType);
            return enumType.cast(newValue);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
