package net.modificationstation.stationapi.api.registry.sync.trackers;

import it.unimi.dsi.fastutil.ints.Int2ReferenceMap;
import it.unimi.dsi.fastutil.ints.Int2ReferenceRBTreeMap;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.Listener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.registry.RegistryEntryAddedEvent;
import net.modificationstation.stationapi.api.event.registry.RegistryIdRemapEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager;
import net.modificationstation.stationapi.api.registry.ListenableRegistry;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.util.collection.IdList;

import java.lang.invoke.MethodHandles;
import java.util.Collection;
import java.util.function.Function;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

@EventListener(phase = StationAPI.INTERNAL_PHASE)
public final class StateIdTracker<T, S> {
    static {
        EntrypointManager.registerLookup(MethodHandles.lookup());
    }

    private final Registry<T> registry;
    private final IdList<S> stateList;
    private final Function<T, Collection<S>> stateGetter;
    private int currentHighestId = 0;

    public static <T, S, R extends Registry<T> & ListenableRegistry> void register(R registry, IdList<S> stateList, Function<T, Collection<S>> stateGetter) {
        StateIdTracker<T, S> tracker = new StateIdTracker<>(registry, stateList, stateGetter);
        registry.getEventBus().register(Listener.object()
                .listener(tracker)
                .build());
    }

    private StateIdTracker(Registry<T> registry, IdList<S> stateList, Function<T, Collection<S>> stateGetter) {
        this.registry = registry;
        this.stateList = stateList;
        this.stateGetter = stateGetter;

        recalcHighestId();
    }

    @EventListener
    private void onEntryAdded(RegistryEntryAddedEvent<T> event) {
        if (event.rawId == currentHighestId + 1) {
            stateGetter.apply(event.object).forEach(stateList::add);
            currentHighestId = event.rawId;
        } else {
            LOGGER.debug("Non-sequential RegistryEntryAddedEvent for " + event.object.getClass().getSimpleName() + " ID tracker (at " + event.id + "), forcing state map recalculation...");
            recalcStateMap();
        }
    }

    @EventListener
    private void onRemap(RegistryIdRemapEvent<T> event) {
        recalcStateMap();
    }

    private void recalcStateMap() {
        stateList.clear();

        Int2ReferenceMap<T> sortedBlocks = new Int2ReferenceRBTreeMap<>();

        currentHighestId = 0;
        registry.forEach((t) -> {
            int rawId = registry.getRawId(t);
            currentHighestId = Math.max(currentHighestId, rawId);
            sortedBlocks.put(rawId, t);
        });

        for (T b : sortedBlocks.values()) stateGetter.apply(b).forEach(stateList::add);
    }

    private void recalcHighestId() {
        currentHighestId = 0;

        for (T object : registry) currentHighestId = Math.max(currentHighestId, registry.getRawId(object));
    }
}
