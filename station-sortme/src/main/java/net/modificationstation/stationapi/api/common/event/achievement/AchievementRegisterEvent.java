package net.modificationstation.stationapi.api.common.event.achievement;

import lombok.RequiredArgsConstructor;
import net.minecraft.achievement.Achievement;
import net.mine_diver.unsafeevents.Event;

import java.util.*;

@RequiredArgsConstructor
public class AchievementRegisterEvent extends Event {

    public final List<Achievement> achievements;

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
