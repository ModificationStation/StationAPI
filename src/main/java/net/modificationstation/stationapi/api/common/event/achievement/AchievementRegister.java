package net.modificationstation.stationapi.api.common.event.achievement;

import net.minecraft.achievement.Achievement;
import net.modificationstation.stationapi.api.common.event.GameEventOld;

import java.util.List;
import java.util.function.Consumer;

public interface AchievementRegister {

    GameEventOld<AchievementRegister> EVENT = new GameEventOld<>(AchievementRegister.class,
            listeners ->
                    achievements -> {
                        for (AchievementRegister listener : listeners)
                            listener.registerAchievements(achievements);
                    },
            (Consumer<GameEventOld<AchievementRegister>>) achievementRegister ->
                    achievementRegister.register(achievements -> GameEventOld.EVENT_BUS.post(new Data(achievements)))
    );

    void registerAchievements(List<Achievement> achievements);

    final class Data extends GameEventOld.Data<AchievementRegister> {

        public final List<Achievement> achievements;

        private Data(List<Achievement> achievements) {
            super(EVENT);
            this.achievements = achievements;
        }
    }
}
