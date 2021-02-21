package net.modificationstation.stationapi.api.client.event.gui.screen.menu;

import lombok.Getter;
import net.minecraft.achievement.Achievement;
import net.minecraft.client.gui.screen.menu.Achievements;

public class AchievementsLineRender extends AchievementsEvent {

    @Getter // @Override
    private final boolean cancellable = true;
    public final Achievement achievement;

    public AchievementsLineRender(Achievements achievementsScreen, Achievement achievement) {
        super(achievementsScreen);
        this.achievement = achievement;
    }
}
