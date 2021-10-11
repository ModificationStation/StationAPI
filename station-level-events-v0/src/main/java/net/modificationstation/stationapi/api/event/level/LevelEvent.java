package net.modificationstation.stationapi.api.event.level;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.level.Level;
import net.minecraft.level.chunk.Chunk;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class LevelEvent extends Event {

    public final Level level;

    public static class Init extends LevelEvent {

        public Init(Level level) {
            super(level);
        }

        @Override
        protected int getEventID() {
            return ID;
        }

        public static final int ID = NEXT_ID.incrementAndGet();
    }

    public static class BlockSet extends LevelEvent {
        
        @Getter
        private final boolean cancellable = true;

        public final Chunk chunk;
        public final int
                x, y, z,
                blockId, blockMeta;

        public BlockSet(Level level, Chunk chunk, int x, int y, int z, int blockId) {
            this(level, chunk, x, y, z, blockId, 0);
        }

        public BlockSet(Level level, Chunk chunk, int x, int y, int z, int blockId, int blockMeta) {
            super(level);
            this.chunk = chunk;
            this.x = x;
            this.y = y;
            this.z = z;
            this.blockId = blockId;
            this.blockMeta = blockMeta;
        }

        @Override
        protected int getEventID() {
            return ID;
        }

        public static final int ID = NEXT_ID.incrementAndGet();
    }
}
