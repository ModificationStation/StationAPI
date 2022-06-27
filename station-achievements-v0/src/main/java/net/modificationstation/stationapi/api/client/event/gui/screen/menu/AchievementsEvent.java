package net.modificationstation.stationapi.api.client.event.gui.screen.menu;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.achievement.Achievement;
import net.minecraft.client.gui.screen.menu.Achievements;

import java.util.Random;

@SuperBuilder
public abstract class AchievementsEvent extends Event {

    public final Achievements achievementsScreen;

    @SuperBuilder
    public static class BackgroundTextureRender extends AchievementsEvent {

        public final Random random;
        public final int column, row, randomizedRow;
        public int backgroundTexture;

        @Override
        protected int getEventID() {
            return ID;
        }

        public static final int ID = NEXT_ID.incrementAndGet();
    }

    @SuperBuilder
    public static class AchievementIconRender extends AchievementsEvent {

        @Getter // @Override
        private final boolean cancelable = true;
        public final Achievement achievement;

        @Override
        protected int getEventID() {
            return ID;
        }

        public static final int ID = NEXT_ID.incrementAndGet();
    }

    @SuperBuilder
    public static class LineRender extends AchievementsEvent {

        @Getter // @Override
        private final boolean cancelable = true;
        public final Achievement achievement;

        @Override
        protected int getEventID() {
            return ID;
        }

        public static final int ID = NEXT_ID.incrementAndGet();
    }
}
