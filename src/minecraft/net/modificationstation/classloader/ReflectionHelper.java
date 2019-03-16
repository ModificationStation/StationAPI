package net.modificationstation.classloader;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectionHelper
{
    @SuppressWarnings("serial")
	public static class UnableToFindMethodException extends RuntimeException
    {
        @SuppressWarnings("unused")
		private String[] methodNames;

        public UnableToFindMethodException(String[] methodNames, Exception failed)
        {
            super(failed);
            this.methodNames = methodNames;
        }

    }

    @SuppressWarnings("serial")
	public static class UnableToFindClassException extends RuntimeException
    {
        @SuppressWarnings("unused")
		private String[] classNames;

        public UnableToFindClassException(String[] classNames, Exception err)
        {
            super(err);
            this.classNames = classNames;
        }

    }

    @SuppressWarnings("serial")
	public static class UnableToAccessFieldException extends RuntimeException
    {

        @SuppressWarnings("unused")
		private String[] fieldNameList;

        public UnableToAccessFieldException(String[] fieldNames, Exception e)
        {
            super(e);
            this.fieldNameList = fieldNames;
        }
    }

    @SuppressWarnings("serial")
	public static class UnableToFindFieldException extends RuntimeException
    {
        @SuppressWarnings("unused")
		private String[] fieldNameList;
        public UnableToFindFieldException(String[] fieldNameList, Exception e)
        {
            super(e);
            this.fieldNameList = fieldNameList;
        }
    }

    public static Field findField(Class<?> clazz, String... fieldNames)
    {
        Exception failed = null;
        for (String fieldName : fieldNames)
        {
            try
            {
                Field f = clazz.getDeclaredField(fieldName);
                f.setAccessible(true);
                return f;
            }
            catch (Exception e)
            {
                failed = e;
            }
        }
        throw new UnableToFindFieldException(fieldNames, failed);
    }

    @SuppressWarnings("unchecked")
    public static <T, E> T getPrivateValue(Class <? super E > classToAccess, E instance, int fieldIndex)
    {
        try
        {
            Field f = classToAccess.getDeclaredFields()[fieldIndex];
            f.setAccessible(true);
            return (T) f.get(instance);
        }
        catch (Exception e)
        {
            throw new UnableToAccessFieldException(new String[0], e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T, E> T getPrivateValue(Class <? super E > classToAccess, E instance, String... fieldNames)
    {
        try
        {
            return (T) findField(classToAccess, fieldNames).get(instance);
        }
        catch (Exception e)
        {
            throw new UnableToAccessFieldException(fieldNames, e);
        }
    }

    public static <T, E> void setPrivateValue(Class <? super T > classToAccess, T instance, E value, int fieldIndex)
    {
        try
        {
            Field f = classToAccess.getDeclaredFields()[fieldIndex];
            f.setAccessible(true);
            f.set(instance, value);
        }
        catch (Exception e)
        {
            throw new UnableToAccessFieldException(new String[0] , e);
        }
    }

    public static <T, E> void setPrivateValue(Class <? super T > classToAccess, T instance, E value, String... fieldNames)
    {
        try
        {
            findField(classToAccess, fieldNames).set(instance, value);
        }
        catch (Exception e)
        {
            throw new UnableToAccessFieldException(fieldNames, e);
        }
    }

    @SuppressWarnings("unchecked")
    public static Class<? super Object> getClass(ClassLoader loader, String... classNames)
    {
        Exception err = null;
        for (String className : classNames)
        {
            try
            {
                return (Class<? super Object>) Class.forName(className, false, loader);
            }
            catch (Exception e)
            {
                err = e;
            }
        }

        throw new UnableToFindClassException(classNames, err);
    }


    public static <E> Method findMethod(Class<? super E> clazz, E instance, String[] methodNames, Class<?>... methodTypes)
    {
        Exception failed = null;
        for (String methodName : methodNames)
        {
            try
            {
                Method m = clazz.getDeclaredMethod(methodName, methodTypes);
                m.setAccessible(true);
                return m;
            }
            catch (Exception e)
            {
                failed = e;
            }
        }
        throw new UnableToFindMethodException(methodNames, failed);
    }

    public static Method[] getMethodsAnnotation(final Class<?> clazz, final Class<? extends Annotation> annotation, final Class<?>...parameterTypes) {
        List<Method> methods = new ArrayList<Method>();
        for (Method m : clazz.getDeclaredMethods()) {
            for (Annotation a : m.getAnnotations()) {
                if (a.annotationType().equals(annotation) && m.getParameterTypes().length == parameterTypes.length && Arrays.asList(m.getParameterTypes()).equals(Arrays.asList(parameterTypes))){
                    methods.add(m);
                }
            }
        }
        return (Method[])methods.toArray(new Method[methods.size()]);
    }
    
    public static Field[] getFieldsAnnotation(final Class<?> clazz, final Class<? extends Annotation> annotation) {
        List<Field> fields = new ArrayList<Field>();
        for (Field f : clazz.getDeclaredFields()) {
            for (Annotation a : f.getAnnotations()) {
                if (a.annotationType().equals(annotation)){
                    fields.add(f);
                }
            }
        }
        return (Field[])fields.toArray(new Field[fields.size()]);
    }
    
    public static <T, E> void setFinalValue(Class <? super T > classToAccess, T instance, E value, int fieldIndex) {
    	try {
    		Field f = classToAccess.getDeclaredFields()[fieldIndex];
    		f.setAccessible(true);
    		Field modifiers = Field.class.getDeclaredField("modifiers");
    		modifiers.setAccessible(true);
    		modifiers.set(f, f.getModifiers() & ~Modifier.FINAL);
    		f.set(instance, value);
    	} catch (Exception e) {
    		throw new UnableToAccessFieldException(new String[0], e);
    	}
    }
    
    public static <T, E> void setFinalValue(Class <? super T > classToAccess, T instance, E value, String... fieldNames) {
    	try {
    		Field f = findField(classToAccess, fieldNames);
    		f.setAccessible(true);
    		Field modifiers = Field.class.getDeclaredField("modifiers");
    		modifiers.setAccessible(true);
    		modifiers.set(f, f.getModifiers() & ~Modifier.FINAL);
    		f.set(instance, value);
    	} catch (Exception e) {
    		throw new UnableToAccessFieldException(new String[0], e);
    	}
    }
}
