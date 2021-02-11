package net.modificationstation.stationapi.api.common.event.item;

import net.modificationstation.stationapi.api.common.event.ModEventOld;
import net.modificationstation.stationapi.api.common.item.ItemRegistry;
import net.modificationstation.stationapi.api.common.registry.ModID;

public interface ItemRegister {

    ModEventOld<ItemRegister> EVENT = new ModEventOld<>(ItemRegister.class,
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
                    itemRegister.register((registry, modID) -> ModEventOld.post(new Data(registry)), null)
    );

    void registerItems(ItemRegistry registry, ModID modID);

    final class Data extends ModEventOld.Data<ItemRegister> {

        public final ItemRegistry registry;

        private Data(ItemRegistry registry) {
            super(EVENT);
            this.registry = registry;
        }
    }
}
