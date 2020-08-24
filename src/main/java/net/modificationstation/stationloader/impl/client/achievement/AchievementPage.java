package net.modificationstation.stationloader.impl.client.achievement;

import net.minecraft.achievement.Achievement;
import net.minecraft.achievement.Achievements;
import net.minecraft.block.BlockBase;

import java.util.ArrayList;
import java.util.Random;

/**
 * Instantiates and adds an achievement page to the achievement page list.
 */
public class AchievementPage {
    public final String pageName;
    private ArrayList<Integer> achievementIds = new ArrayList<>();

    /**
     * @param pageName The name of the page that is shown on the achievements screen.
     */
    public AchievementPage(String pageName) {
        this.pageName = pageName;
        AchievementPageManager.addPage(this);
    }

    /**
     * Adds all provided achievement objects to the achievement page.
     * @param achievements The achievements to be added. Must be properly configured before adding.
     * @see Achievement
     */
    public void addAchievements(Achievement... achievements) {
        for (Achievement achievement : achievements) {
            achievementIds.add(achievement.ID);
            Achievements.ACHIEVEMENTS.add(achievement);
        }
    }

    /**
     * Adds all provided achievement objects to the achievement page.
     * @param achievements The achievements to be added. Must be properly configured before adding.
     * @see Achievement
     */
    void addAchievements(Object... achievements) {
        for (Object achievement : achievements) {
            achievementIds.add(((Achievement) achievement).ID);
        }
    }

    /*
    /**
     * Gets the background terrain.png index.
     * @return Index of the achievement page background texture.
     *
    public int getBackgroundTexture() {
        return backgroundTexture;
    }*/



    public int getBackgroundTexture(Random random, int i, int j)
    {
        int k = BlockBase.SAND.texture;
        int l = random.nextInt(1 + j) + j / 2;
        if(l > 37 || j == 35)
        {
            k = BlockBase.BEDROCK.texture;
        } else
        if(l == 22)
        {
            k = random.nextInt(2) != 0 ? BlockBase.REDSTONE_ORE.texture : BlockBase.ORE_DIAMOND.texture;
        } else
        if(l == 10)
        {
            k = BlockBase.IRON_ORE.texture;
        } else
        if(l == 8)
        {
            k = BlockBase.COAL_ORE.texture;
        } else
        if(l > 4)
        {
            k = BlockBase.STONE.texture;
        } else
        if(l > 0)
        {
            k = BlockBase.DIRT.texture;
        }
        return k;
    }

    public ArrayList<Integer> getAchievementIds() {
        return achievementIds;
    }
}
