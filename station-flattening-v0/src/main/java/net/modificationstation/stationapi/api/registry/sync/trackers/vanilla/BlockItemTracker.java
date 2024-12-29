package net.modificationstation.stationapi.api.registry.sync.trackers.vanilla;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.Listener;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.event.registry.RegistryEntryAddedEvent;
import net.modificationstation.stationapi.api.registry.ListenableRegistry;
import net.modificationstation.stationapi.api.registry.Registry;

import java.lang.invoke.MethodHandles;

public final class BlockItemTracker {
    static {
        Listener.registerLookup(MethodHandles.lookup());
    }

    private BlockItemTracker() { }

    public static <R extends Registry<Item> & ListenableRegistry> void register(R registry) {
        BlockItemTracker tracker = new BlockItemTracker();
        registry.getEventBus().register(Listener.object().listener(tracker).build());
    }

    @EventListener
    private void onEntryAdded(RegistryEntryAddedEvent<Item> event) {
        if (event.object instanceof BlockItem blockItem)
            blockItem.appendBlocks(Item.BLOCK_ITEMS, event.object);
    }
}
