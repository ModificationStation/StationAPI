package net.modificationstation.stationloader.api.common.event.item;

import lombok.Getter;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.modificationstation.stationloader.api.common.event.SimpleEvent;

import java.util.function.Consumer;

public interface ItemCreation {

    @SuppressWarnings("UnstableApiUsage")
    SimpleEvent<ItemCreation> EVENT = new SimpleEvent<>(ItemCreation.class,
            listeners ->
                    (level, player, createdItem) -> {
                        for (ItemCreation listener : listeners)
                            listener.onItemCreated(level, player, createdItem);
                    }, (Consumer<SimpleEvent<ItemCreation>>) itemCreation ->
            itemCreation.register((level, player, createdItem) -> SimpleEvent.EVENT_BUS.post(new Data(level, player, createdItem)))
    );

    void onItemCreated(Level level, PlayerBase player, ItemInstance createdItem);

    final class Data extends SimpleEvent.Data<ItemCreation> {

        @Getter
        private final Level level;
        @Getter
        private final PlayerBase player;
        @Getter
        private final ItemInstance createdItem;
        private Data(Level level, PlayerBase player, ItemInstance createdItem) {
            super(EVENT);
            this.level = level;
            this.player = player;
            this.createdItem = createdItem;
        }
    }
}
