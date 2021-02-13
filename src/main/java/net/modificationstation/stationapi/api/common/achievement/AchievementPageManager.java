package net.modificationstation.stationapi.api.common.achievement;

import net.minecraft.achievement.Achievement;
import net.modificationstation.stationapi.api.common.event.achievement.AchievementRegister;
import net.modificationstation.stationapi.api.common.util.HasHandler;

import java.util.List;

public interface AchievementPageManager extends HasHandler<AchievementPageManager> {

    AchievementPageManager INSTANCE = new AchievementPageManager() {

        private AchievementPageManager handler;

        @Override
        public void setHandler(AchievementPageManager handler) {
            this.handler = handler;
        }

        @Override
        public void addPage(AchievementPage achievementPage) {
            checkAccess(handler);
            handler.addPage(achievementPage);
        }

        @Override
        public AchievementPage nextPage() {
            checkAccess(handler);
            return handler.nextPage();
        }

        @Override
        public AchievementPage prevPage() {
            checkAccess(handler);
            return handler.prevPage();
        }

        @Override
        public AchievementPage getCurrentPage() {
            checkAccess(handler);
            return handler.getCurrentPage();
        }

        @Override
        public String getCurrentPageName() {
            checkAccess(handler);
            return handler.getCurrentPageName();
        }

        @Override
        public int getCurrentPageIndex() {
            checkAccess(handler);
            return handler.getCurrentPageIndex();
        }

        @Override
        public int getPageCount() {
            checkAccess(handler);
            return handler.getPageCount();
        }
    };

    void addPage(AchievementPage achievementPage);

    AchievementPage nextPage();

    AchievementPage prevPage();

    AchievementPage getCurrentPage();

    String getCurrentPageName();

    int getCurrentPageIndex();

    int getPageCount();
}
