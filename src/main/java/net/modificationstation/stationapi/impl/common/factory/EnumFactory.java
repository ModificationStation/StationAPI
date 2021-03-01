package net.modificationstation.stationapi.impl.common.factory;

import sun.reflect.ConstructorAccessor;
import sun.reflect.FieldAccessor;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.*;
import java.util.*;

public class EnumFactory implements net.modificationstation.stationapi.api.common.factory.EnumFactory {

    private static final ReflectionFactory reflectionFactory;

    static {
        try {
            reflectionFactory = ReflectionFactory.getReflectionFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ConstructorAccessor getConstructorAccessor(Class<?> enumClass, Class<?>[] additionalParameterTypes) throws Exception {
        Class<?>[] parameterTypes = new Class[additionalParameterTypes.length + 2];
        parameterTypes[0] = String.class;
        parameterTypes[1] = int.class;
        System.arraycopy(additionalParameterTypes, 0, parameterTypes, 2, additionalParameterTypes.length);
        return reflectionFactory.newConstructorAccessor(enumClass.getDeclaredConstructor(parameterTypes));
    }

    private <T extends Enum<?>> T makeEnum(Class<T> enumClass, String value, int ordinal, Class<?>[] additionalTypes, Object[] additionalValues) throws Exception {
        Object[] parms = new Object[additionalValues.length + 2];
        parms[0] = value;
        parms[1] = ordinal;
        System.arraycopy(additionalValues, 0, parms, 2, additionalValues.length);
        return enumClass.cast(getConstructorAccessor(enumClass, additionalTypes).newInstance(parms));
    }

    public void setFailsafeFieldValue(Field field, Object target, Object value) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        FieldAccessor fieldAccessor = reflectionFactory.newFieldAccessor(field, false);
        fieldAccessor.set(target, value);
    }

    private void blankField(Class<?> enumClass, String fieldName) throws Exception {
        for (Field field : Class.class.getDeclaredFields())
            if (field.getName().contains(fieldName)) {
                field.setAccessible(true);
                setFailsafeFieldValue(field, enumClass, null);
                break;
            }
    }

    private void cleanEnumCache(Class<?> enumClass) throws Exception {
        blankField(enumClass, "enumConstantDirectory");
        blankField(enumClass, "enumConstants");
    }

    @Override
    public <T extends Enum<?>> T addEnum(Class<T> enumType, String enumName, Class<?>[] paramTypes, Object[] paramValues) {
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

        if (valuesField == null)
            return null;

        valuesField.setAccessible(true);

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
