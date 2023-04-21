package net.modificationstation.stationapi.api.registry.sync.trackers;

import it.unimi.dsi.fastutil.ints.Int2ReferenceMap;
import it.unimi.dsi.fastutil.ints.Int2ReferenceRBTreeMap;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.registry.RegistryEntryAddedCallback;
import net.modificationstation.stationapi.api.event.registry.RegistryIdRemapCallback;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ListenableRegistry;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.util.collection.IdList;

import java.util.Collection;
import java.util.function.Function;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

public final class StateIdTracker<T, S> implements RegistryIdRemapCallback<T>, RegistryEntryAddedCallback<T> {
	private final Registry<T> registry;
	private final IdList<S> stateList;
	private final Function<T, Collection<S>> stateGetter;
	private int currentHighestId = 0;

	public static <T, S, R extends Registry<T> & ListenableRegistry<T>> void register(R registry, IdList<S> stateList, Function<T, Collection<S>> stateGetter) {
		StateIdTracker<T, S> tracker = new StateIdTracker<>(registry, stateList, stateGetter);
		RegistryEntryAddedCallback.event(registry).register(StationAPI.INTERNAL_PHASE, tracker);
		RegistryIdRemapCallback.event(registry).register(StationAPI.INTERNAL_PHASE, tracker);
	}

	private StateIdTracker(Registry<T> registry, IdList<S> stateList, Function<T, Collection<S>> stateGetter) {
		this.registry = registry;
		this.stateList = stateList;
		this.stateGetter = stateGetter;

		recalcHighestId();
	}

	@Override
	public void onEntryAdded(int rawId, Identifier id, T object) {
		if (rawId == currentHighestId + 1) {
			stateGetter.apply(object).forEach(stateList::add);
			currentHighestId = rawId;
		} else {
			LOGGER.debug("Non-sequential RegistryEntryAddedCallback for " + object.getClass().getSimpleName() + " ID tracker (at " + id + "), forcing state map recalculation...");
			recalcStateMap();
		}
	}

	@Override
	public void onRemap(RemapState<T> state) {
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
