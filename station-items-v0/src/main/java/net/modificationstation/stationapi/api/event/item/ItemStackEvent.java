package net.modificationstation.stationapi.api.event.item;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;

@SuperBuilder
public abstract class ItemStackEvent extends Event {
    public final ItemInstance itemStack;

    @SuperBuilder
    public static class Crafted extends ItemStackEvent {
        public final Level level;
        public final PlayerBase player;
    }
}
