package net.modificationstation.stationloader.impl.client.achievement;

import net.minecraft.achievement.Achievements;

import java.util.ArrayList;

public class AchievementPageManager {
    private static ArrayList<AchievementPage> ACHIEVEMENT_PAGES = new ArrayList<>();
    private static int currentPage = 0;

    static void addPage(AchievementPage achievementPage) {
        ACHIEVEMENT_PAGES.add(achievementPage);
    }

    public static AchievementPage nextPage() {
        currentPage += 1;
        if (currentPage > ACHIEVEMENT_PAGES.size() - 1) {
            currentPage = 0;
        }
        System.out.println(currentPage);
        return ACHIEVEMENT_PAGES.get(currentPage);
    }

    public static AchievementPage prevPage() {
        currentPage -= 1;
        if (currentPage < 0) {
            currentPage = ACHIEVEMENT_PAGES.size() - 1;
        }
        System.out.println(currentPage);
        return ACHIEVEMENT_PAGES.get(currentPage);
    }

    public static AchievementPage getCurrentPage() {
        return ACHIEVEMENT_PAGES.get(currentPage);
    }

    public static String getCurrentPageName() {
        return getCurrentPage().pageName;
    }

    public static int getCurrentPageIndex() {
        return currentPage;
    }

    public static int getPageCount() {
        return ACHIEVEMENT_PAGES.size();
    }

    static {
        AchievementPage page = new AchievementPage("Minecraft");
        page.addAchievements(Achievements.ACHIEVEMENTS.toArray());
    }
}
