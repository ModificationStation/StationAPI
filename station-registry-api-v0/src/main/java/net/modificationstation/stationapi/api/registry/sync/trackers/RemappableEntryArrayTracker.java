package net.modificationstation.stationapi.api.registry.sync.trackers;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.registry.RegistryEntryAddedCallback;
import net.modificationstation.stationapi.api.event.registry.RegistryIdRemapCallback;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ListenableRegistry;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.registry.RemappableRawIdHolder;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class RemappableEntryArrayTracker<T extends RemappableRawIdHolder> implements RegistryIdRemapCallback<T>, RegistryEntryAddedCallback<T> {
    private final Supplier<T[]> arrayGetter;
    private final Consumer<T[]> arraySetter;

    public static <T extends RemappableRawIdHolder, R extends Registry<T> & ListenableRegistry<T>> void register(
            R registry,
            Supplier<T[]> arrayGetter,
            Consumer<T[]> arraySetter
    ) {
        RemappableEntryArrayTracker<T> tracker = new RemappableEntryArrayTracker<>(arrayGetter, arraySetter);
        RegistryEntryAddedCallback.event(registry).register(StationAPI.INTERNAL_PHASE, tracker);
        RegistryIdRemapCallback.event(registry).register(StationAPI.INTERNAL_PHASE, tracker);
    }

    private RemappableEntryArrayTracker(
            Supplier<T[]> arrayGetter,
            Consumer<T[]> arraySetter
    ) {
        this.arrayGetter = arrayGetter;
        this.arraySetter = arraySetter;
    }

    @Override
    public void onEntryAdded(int rawId, Identifier id, T object) {
        T[] array = arrayGetter.get();
        if (ObjectArrayTracker.shouldGrow(array, rawId))
            arraySetter.accept(ObjectArrayTracker.grow(array, rawId));
    }

    @Override
    public void onRemap(RemapState<T> state) {
        T[] array = arrayGetter.get();
        T[] prevArray = array.clone();
        Arrays.fill(array, null);

        Int2IntMap remap = state.getRawIdChangeMap();
        for (int i = 0; i < prevArray.length; i++) {
            T value = prevArray[i];
            if (value == null) continue;
            int newId = remap.getOrDefault(i, i);
            if (ObjectArrayTracker.shouldGrow(array, newId))
                array = ObjectArrayTracker.grow(array, newId);
            array[newId] = value;
            value.setRawId(newId);
        }

        if (prevArray.length != array.length)
            arraySetter.accept(array);
    }
}
