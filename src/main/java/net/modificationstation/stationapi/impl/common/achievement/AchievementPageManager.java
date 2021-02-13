package net.modificationstation.stationapi.impl.common.achievement;

import net.minecraft.achievement.Achievement;
import net.minecraft.achievement.Achievements;
import net.modificationstation.stationapi.api.common.event.EventListener;
import net.modificationstation.stationapi.api.common.event.ListenerPriority;
import net.modificationstation.stationapi.api.common.event.achievement.AchievementRegister;
import net.modificationstation.stationapi.api.common.factory.GeneralFactory;

import java.util.ArrayList;
import java.util.List;

public class AchievementPageManager implements net.modificationstation.stationapi.api.common.achievement.AchievementPageManager {
    private final ArrayList<net.modificationstation.stationapi.api.common.achievement.AchievementPage> ACHIEVEMENT_PAGES = new ArrayList<>();
    private int currentPage = 0;

    @Override
    public void addPage(net.modificationstation.stationapi.api.common.achievement.AchievementPage achievementPage) {
        ACHIEVEMENT_PAGES.add(achievementPage);
    }

    @Override
    public net.modificationstation.stationapi.api.common.achievement.AchievementPage nextPage() {
        currentPage += 1;
        if (currentPage > ACHIEVEMENT_PAGES.size() - 1) {
            currentPage = 0;
        }
        return ACHIEVEMENT_PAGES.get(currentPage);
    }

    @Override
    public net.modificationstation.stationapi.api.common.achievement.AchievementPage prevPage() {
        currentPage -= 1;
        if (currentPage < 0) {
            currentPage = ACHIEVEMENT_PAGES.size() - 1;
        }
        return ACHIEVEMENT_PAGES.get(currentPage);
    }

    @Override
    public net.modificationstation.stationapi.api.common.achievement.AchievementPage getCurrentPage() {
        return ACHIEVEMENT_PAGES.get(currentPage);
    }

    @Override
    public String getCurrentPageName() {
        return getCurrentPage().getName();
    }

    @Override
    public int getCurrentPageIndex() {
        return currentPage;
    }

    @Override
    public int getPageCount() {
        return ACHIEVEMENT_PAGES.size();
    }

    @EventListener(priority = ListenerPriority.HIGH)
    public void registerAchievements(AchievementRegister event) {
        net.modificationstation.stationapi.api.common.achievement.AchievementPage page = GeneralFactory.INSTANCE.newInst(net.modificationstation.stationapi.api.common.achievement.AchievementPage.class, "Minecraft");
        List<Achievement> list = new ArrayList<>();
        for (Object o : Achievements.ACHIEVEMENTS)
            list.add((Achievement) o);
        page.addAchievements(list.toArray(new Achievement[0]));
    }
}
