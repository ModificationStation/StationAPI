package net.modificationstation.stationloader.api.common.event.item;

import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.modificationstation.stationloader.api.common.event.Event;
import net.modificationstation.stationloader.api.common.factory.EventFactory;

public interface ItemCreation {

    Event<ItemCreation> EVENT = EventFactory.INSTANCE.newEvent(ItemCreation.class, listeners ->
            (level, player, itemInstance) -> {
                for (ItemCreation event : listeners)
                    event.onItemCreated(level, player, itemInstance);
            });

    void onItemCreated(Level level, PlayerBase player, ItemInstance itemInstance);
}
