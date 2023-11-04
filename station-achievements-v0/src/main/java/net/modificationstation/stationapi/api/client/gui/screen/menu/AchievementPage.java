package net.modificationstation.stationapi.api.client.gui.screen.menu;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.ints.IntLists;
import net.minecraft.achievement.Achievement;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import uk.co.benjiweber.expressions.property.Named;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Instantiates and adds an achievement page to the achievement page list.
 * @author calmilamsy
 */
public class AchievementPage implements Named<AchievementPage> {
    private static final List<AchievementPage> PAGES = new ArrayList<>();
    private static int currentPage = 0;

    public static void addPage(AchievementPage achievementPage) {
        PAGES.add(achievementPage);
    }

    public static AchievementPage nextPage() {
        currentPage += 1;
        if (currentPage > PAGES.size() - 1) currentPage = 0;
        return PAGES.get(currentPage);
    }

    public static AchievementPage prevPage() {
        currentPage -= 1;
        if (currentPage < 0) currentPage = PAGES.size() - 1;
        return PAGES.get(currentPage);
    }

    public static AchievementPage getCurrentPage() {
        return PAGES.get(currentPage);
    }

    public static String getCurrentPageName() {
        return getCurrentPage().name();
    }

    public static int getCurrentPageIndex() {
        return currentPage;
    }

    public static int getPageCount() {
        return PAGES.size();
    }

    private final Identifier id;
    private final List<Achievement> achievements = new ArrayList<>();

    public AchievementPage(Identifier id) {
        this.id = id;
        addPage(this);
    }

    /**
     * Adds all provided achievement objects to the achievement page.
     *
     * @param achievements The achievements to be added. Must be properly configured before adding.
     * @see Achievement
     */
    public void addAchievements(Achievement... achievements) {
        Collections.addAll(this.achievements, achievements);
    }

    public int getBackgroundTexture(Random random, int column, int row, int randomizedRow, int currentTexture) {
        return currentTexture;
    }

    @Override
    public String name() {
        return id.toString();
    }

    public List<Achievement> getAchievements() {
        return Collections.unmodifiableList(achievements);
    }

    /**
     * @deprecated Use {@link #AchievementPage(Identifier)} instead.
     *
     * @param namespace the {@link Namespace} to add to the translation key.
     * @param pageName the name of the page that is shown on the achievements screen.
     */
    @Deprecated
    public AchievementPage(Namespace namespace, String pageName) {
        this(namespace.id(pageName));
    }

    /**
     * @deprecated Use {@link #getAchievements()} instead.
     */
    @Deprecated
    public List<Integer> getAchievementIds() {
        return IntLists.unmodifiable(
                achievements
                        .stream()
                        .mapToInt(achievement -> achievement.id)
                        .collect(IntArrayList::new, IntCollection::add, IntCollection::addAll)
        );
    }
}
