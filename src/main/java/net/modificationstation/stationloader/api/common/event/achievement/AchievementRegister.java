package net.modificationstation.stationloader.api.common.event.achievement;

import lombok.Getter;
import net.minecraft.achievement.Achievement;
import net.modificationstation.stationloader.api.common.event.GameEvent;

import java.util.List;
import java.util.function.Consumer;

public interface AchievementRegister {

    @SuppressWarnings("UnstableApiUsage")
    GameEvent<AchievementRegister> EVENT = new GameEvent<>(AchievementRegister.class,
            listeners ->
                    achievements -> {
                        for (AchievementRegister listener : listeners)
                            listener.registerAchievements(achievements);
                    },
            (Consumer<GameEvent<AchievementRegister>>) achievementRegister ->
                    achievementRegister.register(achievements -> GameEvent.EVENT_BUS.post(new Data(achievements)))
    );

    void registerAchievements(List<Achievement> achievements);

    final class Data extends GameEvent.Data<AchievementRegister> {

        @Getter
        private final List<Achievement> achievementList;

        private Data(List<Achievement> achievementList) {
            super(EVENT);
            this.achievementList = achievementList;
        }
    }
}
