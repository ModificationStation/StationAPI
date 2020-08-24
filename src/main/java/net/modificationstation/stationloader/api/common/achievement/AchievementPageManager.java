package net.modificationstation.stationloader.api.common.achievement;

import net.minecraft.achievement.Achievement;
import net.modificationstation.stationloader.api.common.event.achievement.AchievementRegister;
import net.modificationstation.stationloader.api.common.util.HasHandler;

import java.util.List;

public interface AchievementPageManager extends HasHandler<AchievementPageManager>, AchievementRegister {

    AchievementPageManager INSTANCE = new AchievementPageManager() {

        private AchievementPageManager handler;

        @Override
        public void setHandler(AchievementPageManager handler) {
            this.handler = handler;
            AchievementRegister.EVENT.register(handler);
        }

        @Override
        public void registerAchievements(List<Achievement> achievements) {
            if (handler == null)
                throw new RuntimeException("Accessed StationLoader too early!");
            else
                handler.registerAchievements(achievements);
        }

        @Override
        public void addPage(AchievementPage achievementPage) {
            if (handler == null)
                throw new RuntimeException("Accessed StationLoader too early!");
            else
                handler.addPage(achievementPage);
        }

        @Override
        public AchievementPage nextPage() {
            if (handler == null)
                throw new RuntimeException("Accessed StationLoader too early!");
            else
                return handler.nextPage();
        }

        @Override
        public AchievementPage prevPage() {
            if (handler == null)
                throw new RuntimeException("Accessed StationLoader too early!");
            else
                return handler.prevPage();
        }

        @Override
        public AchievementPage getCurrentPage() {
            if (handler == null)
                throw new RuntimeException("Accessed StationLoader too early!");
            else
                return handler.getCurrentPage();
        }

        @Override
        public String getCurrentPageName() {
            if (handler == null)
                throw new RuntimeException("Accessed StationLoader too early!");
            else
                return handler.getCurrentPageName();
        }

        @Override
        public int getCurrentPageIndex() {
            if (handler == null)
                throw new RuntimeException("Accessed StationLoader too early!");
            else
                return handler.getCurrentPageIndex();
        }

        @Override
        public int getPageCount() {
            if (handler == null)
                throw new RuntimeException("Accessed StationLoader too early!");
            else
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
