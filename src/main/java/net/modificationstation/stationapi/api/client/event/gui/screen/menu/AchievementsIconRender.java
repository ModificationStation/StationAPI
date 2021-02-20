package net.modificationstation.stationapi.api.client.event.gui.screen.menu;

import lombok.Getter;
import net.minecraft.achievement.Achievement;
import net.minecraft.client.gui.screen.menu.Achievements;

public class AchievementsIconRender extends AchievementsEvent {

    @Getter(onMethod_ = @Override)
    private final boolean cancellable = true;
    public final Achievement achievement;

    public AchievementsIconRender(Achievements achievementsScreen, Achievement achievement) {
        super(achievementsScreen);
        this.achievement = achievement;
    }
}
