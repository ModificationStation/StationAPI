package net.modificationstation.stationloader.api.common.event.item;

import lombok.Getter;
import net.modificationstation.stationloader.api.common.event.ModEvent;
import net.modificationstation.stationloader.api.common.item.ItemRegistry;
import net.modificationstation.stationloader.api.common.registry.ModID;

public interface ItemRegister {

    @SuppressWarnings("UnstableApiUsage")
    ModEvent<ItemRegister> EVENT = new ModEvent<>(ItemRegister.class,
            listeners ->
                    (registry, modID) -> {
                        for (ItemRegister listener : listeners)
                            listener.registerItems(registry, ItemRegister.EVENT.getListenerModID(listener));
                    },
            listener ->
                    (registry, modID) -> {
                        ItemRegister.EVENT.setCurrentListener(listener);
                        listener.registerItems(registry, modID);
                        ItemRegister.EVENT.setCurrentListener(null);
                    },
            itemRegister ->
                    itemRegister.register((registry, modID) -> ModEvent.EVENT_BUS.post(new Data(registry)), null)
    );

    void registerItems(ItemRegistry registry, ModID modID);

    final class Data extends ModEvent.Data<ItemRegister> {

        @Getter
        private final ItemRegistry registry;

        private Data(ItemRegistry registry) {
            super(EVENT);
            this.registry = registry;
        }
    }
}
