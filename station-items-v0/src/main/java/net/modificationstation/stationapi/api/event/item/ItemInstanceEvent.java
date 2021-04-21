package net.modificationstation.stationapi.api.event.item;

import lombok.RequiredArgsConstructor;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;

@RequiredArgsConstructor
public abstract class ItemInstanceEvent extends Event {

    public final ItemInstance itemInstance;

    public static class Crafted extends ItemInstanceEvent {

        public final Level level;
        public final PlayerBase player;

        public Crafted(ItemInstance itemInstance, Level level, PlayerBase player) {
            super(itemInstance);
            this.level = level;
            this.player = player;
        }

        @Override
        protected int getEventID() {
            return ID;
        }

        public static final int ID = NEXT_ID.incrementAndGet();
    }
}
