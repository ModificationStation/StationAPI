package net.modificationstation.stationapi.api.util;

import com.google.common.collect.ImmutableMap;

import java.util.*;
import java.util.function.*;

public class Util {

    public static <T> T make(T object, Consumer<T> initializer) {
        initializer.accept(object);
        return object;
    }

    public static <T, R> ImmutableMap<T, R> createLookupBy(Function<R, T> keyMapper, R[] values) {
        return Arrays.stream(values).collect(ImmutableMap.toImmutableMap(keyMapper, Function.identity()));
    }
}
