package net.modificationstation.stationapi.impl.client.gui.screen.menu;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.achievement.Achievement;
import net.minecraft.achievement.Achievements;
import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.api.client.event.gui.screen.menu.AchievementsBackgroundTextureOverride;
import net.modificationstation.stationapi.api.client.event.gui.screen.menu.AchievementsIconRender;
import net.modificationstation.stationapi.api.client.event.gui.screen.menu.AchievementsLineRender;
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
    private static void replaceBackgroundTexture(AchievementsBackgroundTextureOverride event) {
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

    @EventListener(priority = ListenerPriority.HIGH)
    private static void renderAchievementIcon(AchievementsIconRender event) {
        if (!isVisibleAchievement(event.achievement))
            event.cancel();
    }

    private static boolean isVisibleAchievement(Achievement achievement) {
        if (checkHidden(achievement)) {
            return false;
        } else if (!AchievementPage.getCurrentPage().getAchievementIds().contains(achievement.ID)) {
            return false;
        } else if (achievement.parent != null && !checkHidden(achievement.parent)) {
            return true;
        } else {
            return true;
        }
    }

    @EventListener(priority = ListenerPriority.HIGH)
    private static void renderAchievementsLine(AchievementsLineRender event) {
        if (!(event.achievement.parent != null && isVisibleAchievement(event.achievement) && isVisibleAchievement(event.achievement.parent)))
            event.cancel();
    }

    private static boolean checkHidden(Achievement achievement) {
        //noinspection deprecation
        if (((Minecraft) FabricLoader.getInstance().getGameInstance()).statFileWriter.isAchievementUnlocked(achievement)) {
            return false;
        }
        if (achievement.parent == null) {
            return false;
        } else {
            return checkHidden(achievement.parent);
        }
    }
}
