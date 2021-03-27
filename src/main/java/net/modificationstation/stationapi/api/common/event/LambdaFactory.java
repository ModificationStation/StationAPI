package net.modificationstation.stationapi.api.common.event;

import java.lang.invoke.*;
import java.lang.reflect.*;
import java.util.function.*;

final class LambdaFactory {

    static <T extends Event> Consumer<T> create(Object target, Method method, Class<T> eventType) {
        method.setAccessible(true);
        MethodHandles.Lookup lookup = IMPL_LOOKUP.in(method.getDeclaringClass());
        MethodHandle methodHandle;
        try {
            methodHandle = lookup.unreflect(method);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Impossible error!", e);
        }
        boolean isStatic = Modifier.isStatic(method.getModifiers());
        if (!isStatic && target == null)
            throw new IllegalArgumentException(String.format("Method %s#%s isn't static, but no listener instance is provided!", method.getDeclaringClass().getName(), method.getName()));
        try {
            MethodHandle factory = LambdaMetafactory.metafactory(lookup, "accept", isStatic ? MethodType.methodType(Consumer.class) : MethodType.methodType(Consumer.class, target.getClass()), MethodType.methodType(void.class, Object.class), methodHandle, MethodType.methodType(void.class, eventType)).getTarget();
            if (!isStatic)
                factory = factory.bindTo(target);
            //noinspection unchecked
            return (Consumer<T>) factory.invoke();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    private static final MethodHandles.Lookup IMPL_LOOKUP;
    static {
        try {
            Field field = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
            field.setAccessible(true);
            IMPL_LOOKUP = (MethodHandles.Lookup) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
