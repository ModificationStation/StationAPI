package net.modificationstation.stationapi.api.common.event.achievement;

import lombok.RequiredArgsConstructor;
import net.minecraft.achievement.Achievement;
import net.modificationstation.stationapi.api.common.event.Event;

import java.util.List;

@RequiredArgsConstructor
public class AchievementRegisterEvent extends Event {

    public final List<Achievement> achievements;
}
