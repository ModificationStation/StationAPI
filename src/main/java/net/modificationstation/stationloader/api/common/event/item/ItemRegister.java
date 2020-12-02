package net.modificationstation.stationloader.api.common.event.item;

import net.modificationstation.stationloader.api.common.event.ModEvent;

public interface ItemRegister {

    ModEvent<ItemRegister> EVENT = new ModEvent<>(ItemRegister.class, listeners ->
            () -> {
                for (ItemRegister listener : listeners) {
                    ItemRegister.EVENT.setCurrentListener(listener);
                    listener.registerItems();
                }
            });

    void registerItems();
}
