package net.modificationstation.stationapi.api.client.event.gui.screen.menu;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.achievement.Achievement;
import net.minecraft.client.gui.screen.menu.Achievements;

import java.util.Random;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AchievementsEvent extends Event {

    public final Achievements achievementsScreen;

    public static class BackgroundTextureRender extends AchievementsEvent {

        public final Random random;
        public final int column, row, randomizedRow;
        public int backgroundTexture;

        public BackgroundTextureRender(Achievements achievements, Random random, int column, int row, int randomizedRow, int backgroundTexture) {
            super(achievements);
            this.random = random;
            this.column = column;
            this.row = row;
            this.randomizedRow = randomizedRow;
            this.backgroundTexture = backgroundTexture;
        }

        @Override
        protected int getEventID() {
            return ID;
        }

        public static final int ID = NEXT_ID.incrementAndGet();
    }

    public static class AchievementIconRender extends AchievementsEvent {

        @Getter // @Override
        private final boolean cancelable = true;
        public final Achievement achievement;

        public AchievementIconRender(Achievements achievementsScreen, Achievement achievement) {
            super(achievementsScreen);
            this.achievement = achievement;
        }

        @Override
        protected int getEventID() {
            return ID;
        }

        public static final int ID = NEXT_ID.incrementAndGet();
    }

    public static class LineRender extends AchievementsEvent {

        @Getter // @Override
        private final boolean cancelable = true;
        public final Achievement achievement;

        public LineRender(Achievements achievementsScreen, Achievement achievement) {
            super(achievementsScreen);
            this.achievement = achievement;
        }

        @Override
        protected int getEventID() {
            return ID;
        }

        public static final int ID = NEXT_ID.incrementAndGet();
    }
}
