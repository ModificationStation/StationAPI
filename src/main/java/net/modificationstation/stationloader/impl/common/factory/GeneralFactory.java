package net.modificationstation.stationloader.impl.common.factory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class GeneralFactory implements net.modificationstation.stationloader.api.common.factory.GeneralFactory {

    @Override
    public boolean hasFactory(Class<?> clazz) {
        return factories.containsKey(clazz);
    }

    @Override
    public <T> T newInst(Class<T> clazz, Object... args) {
        Object o = factories.get(clazz).apply(args);
        if (clazz.isInstance(o))
            return clazz.cast(o);
        else
            throw new RuntimeException("Corrupted factory for class " + clazz.getName() + "!");
    }

    @Override
    public void addFactory(Class<?> clazz, Function<Object[], Object> factory) {
        factories.put(clazz, factory);
    }

    private final Map<Class<?>, Function<Object[], Object>> factories = new HashMap<>();
}
