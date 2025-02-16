package net.modificationstation.stationapi.api.registry.sync.trackers;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.Listener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.registry.RegistryEntryAddedEvent;
import net.modificationstation.stationapi.api.event.registry.RegistryIdRemapEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager;
import net.modificationstation.stationapi.api.registry.ListenableRegistry;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.collection.IdList;

import java.lang.invoke.MethodHandles;

@EventListener(phase = StationAPI.INTERNAL_PHASE)
public final class IdListTracker<V, OV> {
	static {
		EntrypointManager.registerLookup(MethodHandles.lookup());
	}

	private final String name;
	private final IdList<OV> mappers;
	private final Reference2ReferenceMap<Identifier, OV> removedMapperCache = new Reference2ReferenceOpenHashMap<>();

	private IdListTracker(String name, IdList<OV> mappers) {
		this.name = name;
		this.mappers = mappers;
	}

	public static <V, OV, R extends Registry<V> & ListenableRegistry> void register(R registry, String name, IdList<OV> mappers) {
		IdListTracker<V, OV> tracker = new IdListTracker<>(name, mappers);
		registry.getEventBus().register(Listener.object()
				.listener(tracker)
				.build());
	}

	@EventListener
	private void onEntryAdded(RegistryEntryAddedEvent<V> event) {
		if (removedMapperCache.containsKey(event.id)) mappers.set(removedMapperCache.get(event.id), event.rawId);
	}

	@EventListener
	private void onRemap(RegistryIdRemapEvent<V> event) {
		mappers.remapIds(event.state.getRawIdChangeMap());
	}
}