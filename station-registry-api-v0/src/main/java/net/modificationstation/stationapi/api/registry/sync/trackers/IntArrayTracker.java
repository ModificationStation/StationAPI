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

public class IntArrayTracker<T> implements RegistryIdRemapCallback<T>, RegistryEntryAddedCallback<T> {
    private final Supplier<int[]> arrayGetter;
    private final Consumer<int[]> arraySetter;

    public static <T, R extends Registry<T> & ListenableRegistry<T>> void register(
            R registry,
            Supplier<int[]> arrayGetter,
            Consumer<int[]> arraySetter
    ) {
        IntArrayTracker<T> tracker = new IntArrayTracker<>(arrayGetter, arraySetter);
        RegistryEntryAddedCallback.event(registry).register(StationAPI.INTERNAL_PHASE, tracker);
        RegistryIdRemapCallback.event(registry).register(StationAPI.INTERNAL_PHASE, tracker);
    }

    private IntArrayTracker(
            Supplier<int[]> arrayGetter,
            Consumer<int[]> arraySetter
    ) {
        this.arrayGetter = arrayGetter;
        this.arraySetter = arraySetter;
    }

    public static boolean shouldGrow(int[] array, int highestExpectedRawId) {
        return array.length <= highestExpectedRawId;
    }

    public static int[] grow(int[] array, int highestExpectedRawId) {
        return Arrays.copyOf(array, MathHelper.smallestEncompassingPowerOfTwo(highestExpectedRawId + 1));
    }

    @Override
    public void onEntryAdded(int rawId, Identifier id, T object) {
        int[] array = arrayGetter.get();
        if (shouldGrow(array, rawId))
            arraySetter.accept(grow(array, rawId));
    }

    @Override
    public void onRemap(RemapState<T> state) {
        int[] array = arrayGetter.get();
        int[] prevArray = array.clone();
        Arrays.fill(array, 0);

        Int2IntMap remap = state.getRawIdChangeMap();
        for (int i = 0; i < prevArray.length; i++) {
            int value = prevArray[i];
            if (value == 0) continue;
            int newId = remap.getOrDefault(i, i);
            if (shouldGrow(array, newId))
                array = grow(array, newId);
            array[newId] = value;
        }

        if (prevArray.length != array.length)
            arraySetter.accept(array);
    }
}
