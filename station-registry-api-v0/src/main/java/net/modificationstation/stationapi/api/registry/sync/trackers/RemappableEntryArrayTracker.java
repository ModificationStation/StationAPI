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

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Supplier;

@EventListener(phase = StationAPI.INTERNAL_PHASE)
public class RemappableEntryArrayTracker<T extends RemappableRawIdHolder> {
    static {
        Listener.registerLookup(MethodHandles.lookup());
    }

    private final Supplier<T[]> arrayGetter;
    private final Consumer<T[]> arraySetter;

    public static <T extends RemappableRawIdHolder, R extends Registry<T> & ListenableRegistry> void register(
            R registry,
            Supplier<T[]> arrayGetter,
            Consumer<T[]> arraySetter
    ) {
        RemappableEntryArrayTracker<T> tracker = new RemappableEntryArrayTracker<>(arrayGetter, arraySetter);
        registry.getEventBus().register(Listener.object()
                .listener(tracker)
                .build());
    }

    private RemappableEntryArrayTracker(
            Supplier<T[]> arrayGetter,
            Consumer<T[]> arraySetter
    ) {
        this.arrayGetter = arrayGetter;
        this.arraySetter = arraySetter;
    }

    @EventListener
    private void onEntryAdded(RegistryEntryAddedEvent<T> event) {
        T[] array = arrayGetter.get();
        if (ObjectArrayTracker.shouldGrow(array, event.rawId))
            arraySetter.accept(ObjectArrayTracker.grow(array, event.rawId));
    }

    @EventListener
    private void onRemap(RegistryIdRemapEvent<T> event) {
        T[] array = arrayGetter.get();
        T[] prevArray = array.clone();
        Arrays.fill(array, null);

        Int2IntMap remap = event.state.getRawIdChangeMap();
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
