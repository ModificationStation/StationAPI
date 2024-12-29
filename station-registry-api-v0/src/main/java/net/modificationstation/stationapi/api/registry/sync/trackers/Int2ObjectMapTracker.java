package net.modificationstation.stationapi.api.registry.sync.trackers;

import com.google.common.base.Joiner;
import com.mojang.datafixers.util.Either;
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
import net.modificationstation.stationapi.api.registry.RemappableRawIdHolder;
import net.modificationstation.stationapi.api.util.Identifier;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

public class Int2ObjectMapTracker<V, OV> {
	static {
		Listener.registerLookup(MethodHandles.lookup());
	}

	@FunctionalInterface private interface Put<T> { T put(int key, T value); }
	@FunctionalInterface private interface ContainsKey { boolean containsKey(int key); }

	private final String name;
	private final Either<Map<Integer, OV>, Int2ObjectMap<OV>> mappers;
	private final Reference2ObjectMap<Identifier, OV> removedMapperCache = new Reference2ObjectOpenHashMap<>();
	private final boolean remapValues;

	private Int2ObjectMapTracker(String name, Either<Map<Integer, OV>, Int2ObjectMap<OV>> mappers, boolean remapValues) {
		this.name = name;
		this.mappers = mappers;
        this.remapValues = remapValues;
    }

	public static <V, OV, R extends Registry<V> & ListenableRegistry> void register(R registry, String name, Map<Integer, OV> mappers, boolean remapValues) {
        register(registry, new Int2ObjectMapTracker<>(name, Either.left(mappers), remapValues));
	}

	public static <V, OV, R extends Registry<V> & ListenableRegistry> void register(R registry, String name, Int2ObjectMap<OV> mappers, boolean remapValues) {
        register(registry, new Int2ObjectMapTracker<>(name, Either.right(mappers), remapValues));
	}

	private static <V, OV, R extends Registry<V> & ListenableRegistry> void register(R registry, Int2ObjectMapTracker<V, OV> tracker) {
		registry.getEventBus().register(Listener.object()
				.listener(tracker)
				.build());
	}

	@EventListener
	private void onEntryAdded(RegistryEntryAddedEvent<V> event) {
		if (removedMapperCache.containsKey(event.id))
			mappers.<Put<OV>>map(map -> map::put, map -> map::put).put(event.rawId, removedMapperCache.get(event.id));
	}

	@EventListener
	private void onRemap(RegistryIdRemapEvent<V> event) {
		Int2ObjectMap<OV> oldMappers = mappers.map(Int2ObjectOpenHashMap::new, Int2ObjectOpenHashMap::new);
		Int2IntMap remapMap = event.state.getRawIdChangeMap();
		List<String> errors = null;

		mappers.map(Function.identity(), Function.identity()).clear();

		for (int i : oldMappers.keySet()) {
			int newI = remapMap.getOrDefault(i, Integer.MIN_VALUE);

			if (newI >= 0) {
				if (mappers.<ContainsKey>map(map -> map::containsKey, map -> map::containsKey).containsKey(newI)) {
					if (errors == null) errors = new ArrayList<>();

					errors.add(" - Map contained two equal IDs " + newI + " (" + event.state.getIdFromOld(i) + "/" + i + " -> " + event.state.getIdFromNew(newI) + "/" + newI + ")!");
				} else {
					final OV value = oldMappers.get(i);
					mappers.<Put<OV>>map(map -> map::put, map -> map::put).put(newI, value);
					if (remapValues && value instanceof RemappableRawIdHolder holder)
						holder.setRawId(newI);
				}
			} else {
				LOGGER.warn("Int2ObjectMap " + name + " is dropping mapping for integer ID " + i + " (" + event.state.getIdFromOld(i) + ") - should not happen!");
				removedMapperCache.put(event.state.getIdFromOld(i), oldMappers.get(i));
			}
		}

		if (errors != null)
            throw new RuntimeException("Errors while remapping Int2ObjectMap " + name + " found:\n" + Joiner.on('\n').join(errors));
	}
}