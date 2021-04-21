package net.modificationstation.sltest.achievement;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.achievement.Achievement;
import net.minecraft.item.ItemBase;
import net.modificationstation.sltest.SLTest;
import net.modificationstation.sltest.item.ItemListener;
import net.modificationstation.stationapi.api.client.gui.screen.menu.AchievementPage;
import net.modificationstation.stationapi.api.common.event.achievement.AchievementRegisterEvent;

public class AchievementListener {

    @EventListener
    public void registerAchievements(AchievementRegisterEvent event) {
        testAchievementPage = new AchievementPageTest(SLTest.MODID, "testPage");
        testAchievement = new Achievement(69696969, "sltest:testAchievement", 0, 0, ItemListener.testItem, null);
        testAchievementChild = new Achievement(69696970, "sltest:testAchievementChild", 0, 2, ItemBase.goldenApple, testAchievement);
        event.achievements.add(testAchievement);
        event.achievements.add(testAchievementChild);
        testAchievementPage.addAchievements(testAchievement, testAchievementChild);
    }

    public static AchievementPage testAchievementPage;
    public static Achievement testAchievement;
    public static Achievement testAchievementChild;
}
