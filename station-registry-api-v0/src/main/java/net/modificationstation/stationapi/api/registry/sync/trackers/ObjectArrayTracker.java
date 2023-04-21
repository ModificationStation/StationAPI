package net.modificationstation.stationapi.api.registry.sync.trackers;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.registry.RegistryEntryAddedCallback;
import net.modificationstation.stationapi.api.event.registry.RegistryIdRemapCallback;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ListenableRegistry;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.util.math.MathHelper;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ObjectArrayTracker<T, V> implements RegistryIdRemapCallback<T>, RegistryEntryAddedCallback<T> {
    private final Supplier<V[]> arrayGetter;
    private final Consumer<V[]> arraySetter;

    public static <T, V, R extends Registry<T> & ListenableRegistry<T>> void register(
            R registry,
            Supplier<V[]> arrayGetter,
            Consumer<V[]> arraySetter
    ) {
        ObjectArrayTracker<T, V> tracker = new ObjectArrayTracker<>(arrayGetter, arraySetter);
        RegistryEntryAddedCallback.event(registry).register(StationAPI.INTERNAL_PHASE, tracker);
        RegistryIdRemapCallback.event(registry).register(StationAPI.INTERNAL_PHASE, tracker);
    }

    private ObjectArrayTracker(
            Supplier<V[]> arrayGetter,
            Consumer<V[]> arraySetter
    ) {
        this.arrayGetter = arrayGetter;
        this.arraySetter = arraySetter;
    }

    public static <V> boolean shouldGrow(V[] array, int highestExpectedRawId) {
        return array.length <= highestExpectedRawId;
    }

    public static <V> V[] grow(V[] array, int highestExpectedRawId) {
        return Arrays.copyOf(array, MathHelper.smallestEncompassingPowerOfTwo(highestExpectedRawId + 1));
    }

    @Override
    public void onEntryAdded(int rawId, Identifier id, T object) {
        V[] array = arrayGetter.get();
        if (shouldGrow(array, rawId))
            arraySetter.accept(grow(array, rawId));
    }

    @Override
    public void onRemap(RemapState<T> state) {
        V[] array = arrayGetter.get();
        V[] prevArray = array.clone();
        Arrays.fill(array, null);

        Int2IntMap remap = state.getRawIdChangeMap();
        for (int i = 0; i < prevArray.length; i++) {
            V value = prevArray[i];
            if (value == null) continue;
            int newId = remap.getOrDefault(i, i);
            if (shouldGrow(array, newId))
                array = grow(array, newId);
            array[newId] = value;
        }

        if (prevArray.length != array.length)
            arraySetter.accept(array);
    }
}
