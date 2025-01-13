package net.modificationstation.stationapi.api.registry.sync.trackers;

import com.google.common.base.Joiner;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.Listener;
import net.modificationstation.stationapi.api.event.registry.RegistryEntryAddedEvent;
import net.modificationstation.stationapi.api.event.registry.RegistryIdRemapEvent;
import net.modificationstation.stationapi.api.registry.ListenableRegistry;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.ArrayList;
import java.util.List;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

public class Int2ObjectMapTracker<V, OV> {
    private final String name;
    private final Int2ObjectMap<OV> mappers;
    private final Reference2ObjectMap<Identifier, OV> removedMapperCache = new Reference2ObjectOpenHashMap<>();

    private Int2ObjectMapTracker(String name, Int2ObjectMap<OV> mappers) {
        this.name = name;
        this.mappers = mappers;
    }

    public static <V, OV, R extends Registry<V> & ListenableRegistry> void register(R registry, String name, Int2ObjectMap<OV> mappers) {
        Int2ObjectMapTracker<V, OV> tracker = new Int2ObjectMapTracker<>(name, mappers);
        registry.getEventBus().register(Listener.object()
                .listener(tracker)
                .build());
    }

    @EventListener
    private void onEntryAdded(RegistryEntryAddedEvent<V> event) {
        if (removedMapperCache.containsKey(event.id))
            mappers.put(event.rawId, removedMapperCache.get(event.id));
    }

    @EventListener
    private void onRemap(RegistryIdRemapEvent<V> event) {
        Int2ObjectMap<OV> oldMappers = new Int2ObjectOpenHashMap<>(mappers);
        Int2IntMap remapMap = event.state.getRawIdChangeMap();
        List<String> errors = null;

        mappers.clear();

        for (int i : oldMappers.keySet()) {
            int newI = remapMap.getOrDefault(i, Integer.MIN_VALUE);

            if (newI >= 0) {
                if (mappers.containsKey(newI)) {
                    if (errors == null) errors = new ArrayList<>();

                    errors.add(" - Map contained two equal IDs " + newI + " (" + event.state.getIdFromOld(i) + "/" + i + " -> " + event.state.getIdFromNew(newI) + "/" + newI + ")!");
                } else mappers.put(newI, oldMappers.get(i));
            } else {
                LOGGER.warn("Int2ObjectMap " + name + " is dropping mapping for integer ID " + i + " (" + event.state.getIdFromOld(i) + ") - should not happen!");
                removedMapperCache.put(event.state.getIdFromOld(i), oldMappers.get(i));
            }
        }

        if (errors != null)
            throw new RuntimeException("Errors while remapping Int2ObjectMap " + name + " found:\n" + Joiner.on('\n').join(errors));
    }
}