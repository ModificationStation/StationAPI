package net.modificationstation.stationapi.impl.client.gui.screen.menu;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.achievement.Achievement;
import net.minecraft.achievement.Achievements;
import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.api.client.event.gui.screen.menu.AchievementsEvent;
import net.modificationstation.stationapi.api.client.gui.screen.menu.AchievementPage;
import net.modificationstation.stationapi.api.common.StationAPI;
import net.modificationstation.stationapi.api.common.event.EventListener;
import net.modificationstation.stationapi.api.common.event.ListenerPriority;
import net.modificationstation.stationapi.api.common.event.achievement.AchievementRegisterEvent;
import net.modificationstation.stationapi.api.common.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.common.mod.entrypoint.EventBusPolicy;

import java.util.ArrayList;
import java.util.List;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class AchievementPageImpl {

    @EventListener(priority = ListenerPriority.HIGH)
    private static void replaceBackgroundTexture(AchievementsEvent.BackgroundTextureRender event) {
        event.backgroundTexture = AchievementPage.getCurrentPage().getBackgroundTexture(event.random, event.column, event.row, event.randomizedRow, event.backgroundTexture);
    }

    @EventListener(priority = ListenerPriority.HIGH)
    private static void registerAchievements(AchievementRegisterEvent event) {
        AchievementPage page = new AchievementPage(StationAPI.MODID, "minecraft");
        List<Achievement> list = new ArrayList<>();
        for (Object o : Achievements.ACHIEVEMENTS)
            list.add((Achievement) o);
        page.addAchievements(list.toArray(new Achievement[0]));
    }

    @EventListener(priority = ListenerPriority.HIGH)
    private static void renderAchievementIcon(AchievementsEvent.AchievementIconRender event) {
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
    private static void renderAchievementsLine(AchievementsEvent.LineRender event) {
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
