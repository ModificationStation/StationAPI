package net.modificationstation.stationapi.impl.client.gui.screen.menu;

import net.minecraft.achievement.Achievement;
import net.minecraft.achievement.Achievements;
import net.modificationstation.stationapi.api.client.event.gui.screen.menu.AchievementsBackgroundTextureCallback;
import net.modificationstation.stationapi.api.client.gui.screen.menu.AchievementPage;
import net.modificationstation.stationapi.api.common.event.EventListener;
import net.modificationstation.stationapi.api.common.event.ListenerPriority;
import net.modificationstation.stationapi.api.common.event.achievement.AchievementRegister;
import net.modificationstation.stationapi.api.common.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.common.mod.entrypoint.EventBusPolicy;

import java.util.ArrayList;
import java.util.List;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class AchievementPageImpl {

    @EventListener(priority = ListenerPriority.HIGH)
    private static void replaceBackgroundTexture(AchievementsBackgroundTextureCallback event) {
        event.backgroundTexture = AchievementPage.getCurrentPage().getBackgroundTexture(event.random, event.column, event.row, event.randomizedRow, event.backgroundTexture);
    }

    @EventListener(priority = ListenerPriority.HIGH)
    private static void registerAchievements(AchievementRegister event) {
        AchievementPage page = new AchievementPage("Minecraft");
        List<Achievement> list = new ArrayList<>();
        for (Object o : Achievements.ACHIEVEMENTS)
            list.add((Achievement) o);
        page.addAchievements(list.toArray(new Achievement[0]));
    }
}
