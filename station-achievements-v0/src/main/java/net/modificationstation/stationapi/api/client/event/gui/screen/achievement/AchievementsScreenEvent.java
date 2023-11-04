package net.modificationstation.stationapi.api.client.event.gui.screen.achievement;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.mine_diver.unsafeevents.event.Cancelable;
import net.mine_diver.unsafeevents.event.EventPhases;
import net.minecraft.achievement.Achievement;
import net.minecraft.client.gui.screen.achievement.AchievementsScreen;
import net.modificationstation.stationapi.api.StationAPI;

import java.util.Random;

@SuperBuilder
public abstract class AchievementsScreenEvent extends Event {
    public final AchievementsScreen achievementsScreen;

    @SuperBuilder
    @EventPhases(StationAPI.INTERNAL_PHASE)
    public static class BackgroundTextureRender extends AchievementsScreenEvent {
        public final Random random;
        public final int column, row, randomizedRow;
        public int backgroundTexture;
    }

    @Cancelable
    @SuperBuilder
    @EventPhases(StationAPI.INTERNAL_PHASE)
    public static class AchievementIconRender extends AchievementsScreenEvent {
        public final Achievement achievement;
    }

    @Cancelable
    @SuperBuilder
    @EventPhases(StationAPI.INTERNAL_PHASE)
    public static class LineRender extends AchievementsScreenEvent {
        public final Achievement achievement;
    }
}
