package net.modificationstation.stationapi.impl.client.gui.screen.menu;

import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.achievement.Achievement;
import net.minecraft.achievement.Achievements;
import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.gui.screen.menu.AchievementsEvent;
import net.modificationstation.stationapi.api.client.gui.screen.menu.AchievementPage;
import net.modificationstation.stationapi.api.event.achievement.AchievementRegisterEvent;
import net.modificationstation.stationapi.api.event.mod.InitEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.resource.language.LanguageManager;
import net.modificationstation.stationapi.api.util.Null;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public class AchievementPageImpl {
    @Entrypoint.ModID
    public static final ModID MODID = Null.get();
    @Entrypoint.Logger
    public static final Logger LOGGER = Null.get();

    @EventListener
    private static void replaceBackgroundTexture(AchievementsEvent.BackgroundTextureRender event) {
        event.backgroundTexture = AchievementPage.getCurrentPage().getBackgroundTexture(event.random, event.column, event.row, event.randomizedRow, event.backgroundTexture);
    }

    @EventListener
    private static void registerAchievements(AchievementRegisterEvent event) {
        AchievementPage page = new AchievementPage(StationAPI.MODID.id("minecraft"));
        //noinspection unchecked
        page.addAchievements(((List<Achievement>) Achievements.ACHIEVEMENTS).toArray(Achievement[]::new));
    }

    @EventListener
    private static void renderAchievementIcon(AchievementsEvent.AchievementIconRender event) {
        if (!isVisibleAchievement(event.achievement)) event.cancel();
    }

    private static boolean isVisibleAchievement(Achievement achievement) {
        return !checkHidden(achievement) && AchievementPage.getCurrentPage().getAchievements().contains(achievement);
    }

    @EventListener
    private static void renderAchievementsLine(AchievementsEvent.LineRender event) {
        if (!(event.achievement.parent != null && isVisibleAchievement(event.achievement) && isVisibleAchievement(event.achievement.parent)))
            event.cancel();
    }

    private static boolean checkHidden(Achievement achievement) {
        //noinspection deprecation
        return !((Minecraft) FabricLoader.getInstance().getGameInstance()).field_2773.method_1988(achievement)
                && achievement.parent != null && checkHidden(achievement.parent);
    }

    @EventListener
    private static void registerLang(InitEvent event) {
        LOGGER.info("Adding lang folder...");
        LanguageManager.addPath("/assets/" + MODID + "/lang", StationAPI.MODID);
    }
}
