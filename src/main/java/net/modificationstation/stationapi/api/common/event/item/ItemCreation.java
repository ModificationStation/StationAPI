package net.modificationstation.stationapi.api.common.event.item;

import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.common.event.GameEventOld;

import java.util.function.Consumer;

public interface ItemCreation {

    GameEventOld<ItemCreation> EVENT = new GameEventOld<>(ItemCreation.class,
            listeners ->
                    (level, player, createdItem) -> {
                        for (ItemCreation listener : listeners)
                            listener.onItemCreated(level, player, createdItem);
                    },
            (Consumer<GameEventOld<ItemCreation>>) itemCreation ->
                    itemCreation.register((level, player, createdItem) -> GameEventOld.EVENT_BUS.post(new Data(level, player, createdItem)))
    );

    void onItemCreated(Level level, PlayerBase player, ItemInstance createdItem);

    final class Data extends GameEventOld.Data<ItemCreation> {

        public final Level level;
        public final PlayerBase player;
        public final ItemInstance createdItem;

        private Data(Level level, PlayerBase player, ItemInstance createdItem) {
            super(EVENT);
            this.level = level;
            this.player = player;
            this.createdItem = createdItem;
        }
    }
}
