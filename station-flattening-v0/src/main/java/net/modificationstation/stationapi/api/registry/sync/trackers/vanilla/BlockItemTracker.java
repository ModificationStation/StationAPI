package net.modificationstation.stationapi.api.registry.sync.trackers.vanilla;

import net.minecraft.item.Block;
import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.event.registry.RegistryEntryAddedCallback;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ListenableRegistry;
import net.modificationstation.stationapi.api.registry.Registry;

public final class BlockItemTracker implements RegistryEntryAddedCallback<ItemBase> {
	private BlockItemTracker() { }

	public static <R extends Registry<ItemBase> & ListenableRegistry<ItemBase>> void register(R registry) {
		BlockItemTracker tracker = new BlockItemTracker();
		RegistryEntryAddedCallback.event(registry).register(tracker);
	}

	@Override
	public void onEntryAdded(int rawId, Identifier id, ItemBase object) {
		if (object instanceof Block blockItem)
			blockItem.appendBlocks(ItemBase.BLOCK_ITEMS, object);
	}
}
