package net.modificationstation.stationloader.api.common.event.item;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.item.ItemBase;
import net.modificationstation.stationloader.api.common.event.GameEvent;

import java.util.function.Consumer;

public interface ItemNameSet {

    GameEvent<ItemNameSet> EVENT = new GameEvent<>(ItemNameSet.class,
            listeners ->
                    (item, newName) -> {
                        for (ItemNameSet listener : listeners)
                            newName = listener.getName(item, newName);
                        return newName;
                    },
            (Consumer<GameEvent<ItemNameSet>>) itemNameSet ->
                    itemNameSet.register((item, newName) -> {
                        Data data = new Data(item, newName);
                        //noinspection UnstableApiUsage
                        GameEvent.EVENT_BUS.post(data);
                        return data.getNewName();
                    })
    );

    String getName(ItemBase item, String newName);

    final class Data extends GameEvent.Data<ItemNameSet> {

        @Getter
        private final ItemBase item;
        @Getter
        @Setter
        private String newName;

        private Data(ItemBase item, String newName) {
            super(EVENT);
            this.item = item;
            this.newName = newName;
        }
    }
}
