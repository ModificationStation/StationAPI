package net.modificationstation.stationapi.api.client.gui.screen.menu;

import net.minecraft.achievement.Achievement;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.util.API;
import net.modificationstation.stationapi.api.util.Named;

import java.util.*;
import java.util.stream.*;

/**
 * Instantiates and adds an achievement page to the achievement page list.
 * @author calmilamsy
 */
public class AchievementPage implements Named {

    private static final List<AchievementPage> PAGES = new ArrayList<>();
    private static int currentPage = 0;

    private final String pageName;
    private final List<Integer> achievementIds = new ArrayList<>();

    /**
     * @param modID the {@link ModID} to add to the translation key.
     * @param pageName the name of the page that is shown on the achievements screen.
     */
    public AchievementPage(ModID modID, String pageName) {
        this.pageName = Identifier.of(modID, pageName).toString();
        addPage(this);
    }

    public static void addPage(AchievementPage achievementPage) {
        PAGES.add(achievementPage);
    }

    @API
    public static AchievementPage nextPage() {
        currentPage += 1;
        if (currentPage > PAGES.size() - 1) {
            currentPage = 0;
        }
        return PAGES.get(currentPage);
    }

    @API
    public static AchievementPage prevPage() {
        currentPage -= 1;
        if (currentPage < 0) {
            currentPage = PAGES.size() - 1;
        }
        return PAGES.get(currentPage);
    }

    public static AchievementPage getCurrentPage() {
        return PAGES.get(currentPage);
    }

    public static String getCurrentPageName() {
        return getCurrentPage().getName();
    }

    @API
    public static int getCurrentPageIndex() {
        return currentPage;
    }

    public static int getPageCount() {
        return PAGES.size();
    }

    /**
     * Adds all provided achievement objects to the achievement page.
     *
     * @param achievements The achievements to be added. Must be properly configured before adding.
     * @see Achievement
     */
    public void addAchievements(Achievement... achievements) {
        achievementIds.addAll(Arrays.stream(achievements).map(achievement -> achievement.ID).collect(Collectors.toList()));
    }

    public int getBackgroundTexture(Random random, int column, int row, int randomizedRow, int currentTexture) {
        return currentTexture;
    }

    @Override
    public String getName() {
        return pageName;
    }

    @API
    public List<Integer> getAchievementIds() {
        return Collections.unmodifiableList(achievementIds);
    }
}
