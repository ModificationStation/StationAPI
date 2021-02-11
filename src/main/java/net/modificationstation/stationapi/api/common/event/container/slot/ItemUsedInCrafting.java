package net.modificationstation.stationapi.api.common.event.container.slot;

import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.common.event.GameEventOld;

import java.util.function.Consumer;

public interface ItemUsedInCrafting {

    GameEventOld<ItemUsedInCrafting> EVENT = new GameEventOld<>(ItemUsedInCrafting.class,
            listeners ->
                    (player, craftingMatrix, itemOrdinal, itemUsed, itemCrafted) -> {
                        for (ItemUsedInCrafting listener : listeners)
                            listener.onItemUsedInCrafting(player, craftingMatrix, itemOrdinal, itemUsed, itemCrafted);
                    },
            (Consumer<GameEventOld<ItemUsedInCrafting>>) itemUsedInCrafting ->
                    itemUsedInCrafting.register((player, craftingMatrix, itemOrdinal, itemUsed, itemCrafted) -> GameEventOld.EVENT_BUS.post(new Data(player, craftingMatrix, itemOrdinal, itemUsed, itemCrafted)))
    );

    void onItemUsedInCrafting(PlayerBase player, InventoryBase craftingMatrix, int itemOrdinal, ItemInstance itemUsed, ItemInstance itemCrafted);

    final class Data extends GameEventOld.Data<ItemUsedInCrafting> {

        public final PlayerBase player;
        public final InventoryBase craftingMatrix;
        public final int itemOrdinal;
        public final ItemInstance itemUsed;
        public final ItemInstance itemCrafted;

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
