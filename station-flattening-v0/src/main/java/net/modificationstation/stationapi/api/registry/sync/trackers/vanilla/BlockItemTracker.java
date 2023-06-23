package net.modificationstation.stationapi.api.registry.sync.trackers.vanilla;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.Listener;
import net.minecraft.item.Block;
import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.event.registry.RegistryEntryAddedEvent;
import net.modificationstation.stationapi.api.registry.ListenableRegistry;
import net.modificationstation.stationapi.api.registry.Registry;

public final class BlockItemTracker {
	private BlockItemTracker() { }

	public static <R extends Registry<ItemBase> & ListenableRegistry> void register(R registry) {
		BlockItemTracker tracker = new BlockItemTracker();
		registry.getEventBus().register(Listener.object().listener(tracker).build());
	}

	@EventListener
	private void onEntryAdded(RegistryEntryAddedEvent<ItemBase> event) {
		if (event.object instanceof Block blockItem)
			blockItem.appendBlocks(ItemBase.BLOCK_ITEMS, event.object);
	}
}
