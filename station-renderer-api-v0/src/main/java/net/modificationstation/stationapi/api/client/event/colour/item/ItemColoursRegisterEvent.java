package net.modificationstation.stationapi.api.client.event.colour.item;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.mine_diver.unsafeevents.Event;
import net.modificationstation.stationapi.api.client.colour.block.BlockColours;
import net.modificationstation.stationapi.api.client.colour.item.ItemColours;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemColoursRegisterEvent extends Event {

    @Getter
    private BlockColours blockColours;
    @Getter
    private ItemColours itemColours;

    private static final Maker MAKER = new Maker(new ItemColoursRegisterEvent());

    public static Maker maker() {
        return MAKER.clear();
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Maker {

        private final ItemColoursRegisterEvent event;

        private Maker clear() {
            event.blockColours = null;
            event.itemColours = null;
            return this;
        }

        public Maker blockColours(BlockColours blockColours) {
            event.blockColours = blockColours;
            return this;
        }

        public Maker itemColours(ItemColours itemColours) {
            event.itemColours = itemColours;
            return this;
        }

        public ItemColoursRegisterEvent make() {
            return event;
        }
    }

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
