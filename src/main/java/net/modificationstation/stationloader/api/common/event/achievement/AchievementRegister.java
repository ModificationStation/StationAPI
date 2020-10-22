package net.modificationstation.stationloader.api.common.event.achievement;

import net.minecraft.achievement.Achievement;
import net.modificationstation.stationloader.api.common.event.Event;
import net.modificationstation.stationloader.api.common.factory.EventFactory;

import java.util.List;

public interface AchievementRegister {

    Event<AchievementRegister> EVENT = EventFactory.INSTANCE.newEvent(AchievementRegister.class, listeners ->
            achievements -> {
                for (AchievementRegister event : listeners)
                    event.registerAchievements(achievements);
            });

    void registerAchievements(List<Achievement> achievements);
}
