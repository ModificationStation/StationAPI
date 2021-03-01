package net.modificationstation.stationapi.api.client.event.gui.screen.menu;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.achievement.Achievement;
import net.minecraft.client.gui.screen.menu.Achievements;
import net.modificationstation.stationapi.api.common.event.Event;

import java.util.*;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AchievementsEvent extends Event {

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
    }

    public static class AchievementIconRender extends AchievementsEvent {

        @Getter // @Override
        private final boolean cancellable = true;
        public final Achievement achievement;

        public AchievementIconRender(Achievements achievementsScreen, Achievement achievement) {
            super(achievementsScreen);
            this.achievement = achievement;
        }
    }

    public static class LineRender extends AchievementsEvent {

        @Getter // @Override
        private final boolean cancellable = true;
        public final Achievement achievement;

        public LineRender(Achievements achievementsScreen, Achievement achievement) {
            super(achievementsScreen);
            this.achievement = achievement;
        }
    }
}
