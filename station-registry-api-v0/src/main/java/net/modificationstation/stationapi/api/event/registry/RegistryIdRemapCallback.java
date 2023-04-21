package net.modificationstation.stationapi.api.event.registry;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import net.fabricmc.fabric.api.event.Event;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ListenableRegistry;

/**
 * The remapping process functions as follows:
 *
 * <ul><li>RegistryEntryRemovedCallbacks are called to remove any objects culled in the process, with the old numeric ID.
 * <li>RegistryIdRemapCallback is emitted to allow remapping the IDs of objects still present.
 * <li>RegistryEntryAddedCallbacks are called to add any objects added in the process, with the new numeric ID.</ul>
 *
 * <p>RegistryIdRemapCallback is called on every remapping operation, if you want to do your own processing in one swoop
 * (say, rebuild the ID map from scratch).
 *
 * <p>Generally speaking, a remap can only cause object *removals*; object *additions* are necessary to reverse remaps.
 *
 * @param <T> The registry type.
 */
@FunctionalInterface
public interface RegistryIdRemapCallback<T> {
	void onRemap(RemapState<T> state);

	interface RemapState<T> {
		Int2IntMap getRawIdChangeMap();
		Identifier getIdFromOld(int oldRawId);
		Identifier getIdFromNew(int newRawId);
	}

	static <T> Event<RegistryIdRemapCallback<T>> event(ListenableRegistry<T> registry) {
		return registry.getRemapEvent();
	}
}
