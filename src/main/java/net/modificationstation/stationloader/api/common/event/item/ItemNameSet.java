package net.modificationstation.stationloader.api.common.event.item;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.item.ItemBase;
import net.modificationstation.stationloader.api.common.event.SimpleEvent;

import java.util.function.Consumer;

public interface ItemNameSet {

    SimpleEvent<ItemNameSet> EVENT = new SimpleEvent<>(ItemNameSet.class,
            listeners ->
                    (item, newName) -> {
        for (ItemNameSet listener : listeners)
            newName = listener.getName(item, newName);
        return newName;
    }, (Consumer<SimpleEvent<ItemNameSet>>) itemNameSet ->
            itemNameSet.register((item, newName) -> {
                Data data = new Data(item, newName);
                //noinspection UnstableApiUsage
                SimpleEvent.EVENT_BUS.post(data);
                return data.getNewName();
            })
    );

    String getName(ItemBase item, String newName);

    final class Data extends SimpleEvent.Data<ItemNameSet> {

        private Data(ItemBase item, String newName) {
            super(EVENT);
            this.item = item;
            this.newName = newName;
        }

        @Getter
        private final ItemBase item;
        @Getter @Setter
        private String newName;
    }
}
