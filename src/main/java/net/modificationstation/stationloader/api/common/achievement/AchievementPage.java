package net.modificationstation.stationloader.api.common.achievement;

import net.minecraft.achievement.Achievement;
import net.modificationstation.stationloader.api.common.util.Named;

import java.util.ArrayList;
import java.util.Random;

public interface AchievementPage extends Named {

    void addAchievements(Achievement... achievements);

    int getBackgroundTexture(Random random, int i, int j);

    ArrayList<Integer> getAchievementIds();
}
