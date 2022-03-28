package net.modificationstation.stationapi.api.client.event.colour.block;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.mine_diver.unsafeevents.Event;
import net.modificationstation.stationapi.api.client.colour.block.BlockColours;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BlockColoursRegisterEvent extends Event {

    @Getter
    private BlockColours blockColours;

    private static final Maker MAKER = new Maker(new BlockColoursRegisterEvent());

    public static Maker maker() {
        return MAKER.clear();
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Maker {

        private final BlockColoursRegisterEvent event;

        private Maker clear() {
            event.blockColours = null;
            return this;
        }

        public Maker blockColours(BlockColours blockColours) {
            event.blockColours = blockColours;
            return this;
        }

        public BlockColoursRegisterEvent make() {
            return event;
        }
    }

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
