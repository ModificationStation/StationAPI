package net.modificationstation.stationloader.api.common.event.container.slot;

import lombok.Getter;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationloader.api.common.event.GameEvent;

import java.util.function.Consumer;

public interface ItemUsedInCrafting {

    GameEvent<ItemUsedInCrafting> EVENT = new GameEvent<>(ItemUsedInCrafting.class,
            listeners ->
                    (player, craftingMatrix, itemOrdinal, itemUsed, itemCrafted) -> {
                        for (ItemUsedInCrafting listener : listeners)
                            listener.onItemUsedInCrafting(player, craftingMatrix, itemOrdinal, itemUsed, itemCrafted);
                    },
            (Consumer<GameEvent<ItemUsedInCrafting>>) itemUsedInCrafting ->
                    itemUsedInCrafting.register((player, craftingMatrix, itemOrdinal, itemUsed, itemCrafted) -> GameEvent.EVENT_BUS.post(new Data(player, craftingMatrix, itemOrdinal, itemUsed, itemCrafted)))
    );

    void onItemUsedInCrafting(PlayerBase player, InventoryBase craftingMatrix, int itemOrdinal, ItemInstance itemUsed, ItemInstance itemCrafted);

    final class Data extends GameEvent.Data<ItemUsedInCrafting> {

        @Getter
        private final PlayerBase player;
        @Getter
        private final InventoryBase craftingMatrix;
        @Getter
        private final int itemOrdinal;
        @Getter
        private final ItemInstance itemUsed;
        @Getter
        private final ItemInstance itemCrafted;

        private Data(PlayerBase player, InventoryBase craftingMatrix, int itemOrdinal, ItemInstance itemUsed, ItemInstance itemCrafted) {
            super(EVENT);
            this.player = player;
            this.craftingMatrix = craftingMatrix;
            this.itemOrdinal = itemOrdinal;
            this.itemUsed = itemUsed;
            this.itemCrafted = itemCrafted;
        }
    }
}
