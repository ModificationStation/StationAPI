package net.modificationstation.stationapi.api.registry.sync.trackers;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.Listener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.registry.RegistryEntryAddedEvent;
import net.modificationstation.stationapi.api.event.registry.RegistryIdRemapEvent;
import net.modificationstation.stationapi.api.registry.ListenableRegistry;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.registry.RemappableRawIdHolder;
import net.modificationstation.stationapi.api.util.math.MathHelper;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Supplier;

@EventListener(phase = StationAPI.INTERNAL_PHASE)
public class ObjectArrayTracker<T, V> {
    static {
        Listener.registerLookup(MethodHandles.lookup());
    }

    private final Supplier<V[]> arrayGetter;
    private final Consumer<V[]> arraySetter;

    public static <T, V, R extends Registry<T> & ListenableRegistry> void register(
            R registry,
            Supplier<V[]> arrayGetter,
            Consumer<V[]> arraySetter
    ) {
        ObjectArrayTracker<T, V> tracker = new ObjectArrayTracker<>(arrayGetter, arraySetter);
        registry.getEventBus().register(Listener.object()
                .listener(tracker)
                .build());
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

    @EventListener
    private void onEntryAdded(RegistryEntryAddedEvent<T> event) {
        V[] array = arrayGetter.get();
        if (shouldGrow(array, event.rawId))
            arraySetter.accept(grow(array, event.rawId));
    }

    @EventListener
    private void onRemap(RegistryIdRemapEvent<T> event) {
        V[] array = arrayGetter.get();
        V[] prevArray = array.clone();
        Arrays.fill(array, null);

        Int2IntMap remap = event.state.getRawIdChangeMap();
        for (int i = 0; i < prevArray.length; i++) {
            V value = prevArray[i];
            if (value == null) continue;
            int newId = remap.getOrDefault(i, i);
            if (shouldGrow(array, newId))
                array = grow(array, newId);
            array[newId] = value;
            if (value instanceof RemappableRawIdHolder holder)
                holder.setRawId(newId);
        }

        if (prevArray.length != array.length)
            arraySetter.accept(array);
    }
}
