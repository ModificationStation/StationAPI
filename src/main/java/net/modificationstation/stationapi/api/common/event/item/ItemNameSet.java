package net.modificationstation.stationapi.api.common.event.item;

import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.common.event.GameEventOld;

import java.util.function.Consumer;

public interface ItemNameSet {

    GameEventOld<ItemNameSet> EVENT = new GameEventOld<>(ItemNameSet.class,
            listeners ->
                    (item, newName) -> {
                        for (ItemNameSet listener : listeners)
                            newName = listener.getName(item, newName);
                        return newName;
                    },
            (Consumer<GameEventOld<ItemNameSet>>) itemNameSet ->
                    itemNameSet.register((item, newName) -> {
                        Data data = new Data(item, newName);
                        GameEventOld.EVENT_BUS.post(data);
                        return data.newName;
                    })
    );

    String getName(ItemBase item, String newName);

    final class Data extends GameEventOld.Data<ItemNameSet> {

        public final ItemBase item;
        public String newName;

        private Data(ItemBase item, String newName) {
            super(EVENT);
            this.item = item;
            this.newName = newName;
        }
    }
}
