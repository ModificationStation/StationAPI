package net.modificationstation.stationapi.impl.lookup;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.modificationstation.stationapi.api.lookup.block.BlockAPILookupEvent;
import net.modificationstation.stationapi.api.lookup.block.BlockEntityAPILookupEvent;
import net.modificationstation.stationapi.api.lookup.item.ItemAPILookupEvent;

public class SelfLookupImpl {

    @EventListener(priority = ListenerPriority.HIGH)
    private static void blockEntitySelfRegister(BlockEntityAPILookupEvent event) {
        if (event.apiClass.isInstance(event.blockEntity)) {
            event.found(event.blockEntity);
        }
    }

    @EventListener(priority = ListenerPriority.HIGH)
    private static void blockSelfRegister(BlockAPILookupEvent event) {
        if (event.apiClass.isInstance(event.block)) {
            event.found(event.block);
        }
    }

    @EventListener(priority = ListenerPriority.HIGH)
    private static void itemSelfRegister(ItemAPILookupEvent event) {
        if (event.apiClass.isInstance(event.itemStack.getItem())) {
            event.found(event.itemStack.getItem());
        }
    }

}
