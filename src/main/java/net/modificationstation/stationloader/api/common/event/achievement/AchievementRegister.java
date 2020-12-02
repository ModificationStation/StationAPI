package net.modificationstation.stationloader.api.common.event.achievement;

import net.minecraft.achievement.Achievement;
import net.modificationstation.stationloader.api.common.event.SimpleEvent;

import java.util.List;

public interface AchievementRegister {

    SimpleEvent<AchievementRegister> EVENT = new SimpleEvent<>(AchievementRegister.class, listeners ->
            achievements -> {
                for (AchievementRegister event : listeners)
                    event.registerAchievements(achievements);
            });

    void registerAchievements(List<Achievement> achievements);
}
