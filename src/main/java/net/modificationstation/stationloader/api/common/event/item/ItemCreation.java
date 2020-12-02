package net.modificationstation.stationloader.api.common.event.item;

import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.modificationstation.stationloader.api.common.event.SimpleEvent;

public interface ItemCreation {

    SimpleEvent<ItemCreation> EVENT = new SimpleEvent<>(ItemCreation.class, listeners ->
            (level, player, itemInstance) -> {
                for (ItemCreation event : listeners)
                    event.onItemCreated(level, player, itemInstance);
            });

    void onItemCreated(Level level, PlayerBase player, ItemInstance itemInstance);
}
