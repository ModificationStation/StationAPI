package net.modificationstation.stationapi.api.registry.sync.trackers;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.Listener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.registry.RegistryEntryAddedEvent;
import net.modificationstation.stationapi.api.event.registry.RegistryIdRemapEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager;
import net.modificationstation.stationapi.api.registry.ListenableRegistry;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.util.math.MathHelper;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Supplier;

@EventListener(phase = StationAPI.INTERNAL_PHASE)
public class IntArrayTracker<T> {
    static {
        EntrypointManager.registerLookup(MethodHandles.lookup());
    }

    private final Supplier<int[]> arrayGetter;
    private final Consumer<int[]> arraySetter;

    public static <T, R extends Registry<T> & ListenableRegistry> void register(
            R registry,
            Supplier<int[]> arrayGetter,
            Consumer<int[]> arraySetter
    ) {
        IntArrayTracker<T> tracker = new IntArrayTracker<>(arrayGetter, arraySetter);
        registry.getEventBus().register(Listener.object()
                .listener(tracker)
                .build());
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

    @EventListener
    private void onEntryAdded(RegistryEntryAddedEvent<T> event) {
        int[] array = arrayGetter.get();
        if (shouldGrow(array, event.rawId))
            arraySetter.accept(grow(array, event.rawId));
    }

    @EventListener
    private void onRemap(RegistryIdRemapEvent<T> event) {
        int[] array = arrayGetter.get();
        int[] prevArray = array.clone();
        Arrays.fill(array, 0);

        Int2IntMap remap = event.state.getRawIdChangeMap();
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
