package net.modificationstation.sltest.achievement;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.achievement.Achievement;
import net.modificationstation.sltest.block.Blocks;
import net.modificationstation.sltest.item.ItemListener;
import net.modificationstation.stationapi.api.client.gui.screen.achievement.AchievementPage;
import net.modificationstation.stationapi.api.event.achievement.AchievementRegisterEvent;
import net.modificationstation.stationapi.api.template.achievement.TemplateAchievement;

import static net.modificationstation.sltest.SLTest.NAMESPACE;

public class AchievementListener {
    public static AchievementPage testAchievementPage;
    public static Achievement
            testAchievement,
            testAchievementChild;

    @EventListener
    public void registerAchievements(AchievementRegisterEvent event) {
        testAchievementPage = new AchievementPageTest(NAMESPACE.id("testPage"));
        testAchievement = new TemplateAchievement(NAMESPACE.id("test_achievement"), "sltest.testAchievement", 0, 0, ItemListener.testItem, null);
        testAchievementChild = new TemplateAchievement(NAMESPACE.id("test_achievement_child"), "sltest.testAchievementChild", 0, 2, Blocks.FREEZER.get(), testAchievement);
        event.achievements.add(testAchievement);
        event.achievements.add(testAchievementChild);
        testAchievementPage.addAchievements(testAchievement, testAchievementChild);
    }
}
