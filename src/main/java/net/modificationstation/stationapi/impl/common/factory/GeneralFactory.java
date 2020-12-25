package net.modificationstation.stationapi.impl.common.factory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class GeneralFactory implements net.modificationstation.stationapi.api.common.factory.GeneralFactory {

    private final Map<Class<?>, Function<Object[], ?>> factories = new HashMap<>();

    @Override
    public boolean hasFactory(Class<?> clazz) {
        return factories.containsKey(clazz);
    }

    @Override
    public <T> T newInst(Class<T> clazz, Object... args) {
        Object o = factories.get(clazz).apply(args);
        if (o == null)
            throw new RuntimeException("Null instance for class " + clazz.getName() + "!");
        else if (clazz.isInstance(o))
            return clazz.cast(o);
        else
            throw new RuntimeException("Corrupted factory for class " + clazz.getName() + "!");
    }

    @Override
    public <T> void addFactory(Class<T> clazz, Function<Object[], T> factory) {
        factories.put(clazz, factory);
    }
}
