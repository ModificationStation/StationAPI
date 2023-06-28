package net.modificationstation.stationapi.api.client.event.gui.screen.menu;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.mine_diver.unsafeevents.event.Cancelable;
import net.mine_diver.unsafeevents.event.EventPhases;
import net.minecraft.achievement.Achievement;
import net.minecraft.client.gui.screen.menu.Achievements;
import net.modificationstation.stationapi.api.StationAPI;

import java.util.Random;

@SuperBuilder
public abstract class AchievementsEvent extends Event {
    public final Achievements achievementsScreen;

    @SuperBuilder
    @EventPhases(StationAPI.INTERNAL_PHASE)
    public static class BackgroundTextureRender extends AchievementsEvent {
        public final Random random;
        public final int column, row, randomizedRow;
        public int backgroundTexture;
    }

    @Cancelable
    @SuperBuilder
    @EventPhases(StationAPI.INTERNAL_PHASE)
    public static class AchievementIconRender extends AchievementsEvent {
        public final Achievement achievement;
    }

    @Cancelable
    @SuperBuilder
    @EventPhases(StationAPI.INTERNAL_PHASE)
    public static class LineRender extends AchievementsEvent {
        public final Achievement achievement;
    }
}
