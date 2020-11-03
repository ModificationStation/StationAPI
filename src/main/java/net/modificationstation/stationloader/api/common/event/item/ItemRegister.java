package net.modificationstation.stationloader.api.common.event.item;

import net.modificationstation.stationloader.api.common.event.ModIDEvent;
import net.modificationstation.stationloader.api.common.factory.EventFactory;

public interface ItemRegister {

    ModIDEvent<ItemRegister> EVENT = EventFactory.INSTANCE.newModIDEvent(ItemRegister.class, listeners ->
            () -> {
                for (ItemRegister listener : listeners) {
                    ItemRegister.EVENT.setCurrentListener(listener);
                    listener.registerItems();
                }
            });

    void registerItems();
}
