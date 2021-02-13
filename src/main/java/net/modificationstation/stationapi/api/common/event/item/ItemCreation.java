package net.modificationstation.stationapi.api.common.event.item;

import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;

public class ItemCreation extends ItemInstanceEvent {

    public final Level level;
    public final PlayerBase player;

    public ItemCreation(ItemInstance itemInstance, Level level, PlayerBase player) {
        super(itemInstance);
        this.level = level;
        this.player = player;
    }
}
