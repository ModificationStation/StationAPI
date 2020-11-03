package net.modificationstation.stationloader.api.common.event.item;

import net.modificationstation.stationloader.api.common.event.ModIDEvent;
import net.modificationstation.stationloader.api.common.factory.EventFactory;

public interface ItemRegister {

    ModIDEvent<ItemRegister> EVENT = EventFactory.INSTANCE.newModIDEvent(ItemRegister.class, listeners ->
            () -> {
                ModIDEvent<ItemRegister> event = ItemRegister.EVENT;
                for (ItemRegister listener : listeners) {
                    event.setCurrentListener(listener);
                    listener.registerItems();
                }
                event.setCurrentListener(null);
            });

    void registerItems();
}
