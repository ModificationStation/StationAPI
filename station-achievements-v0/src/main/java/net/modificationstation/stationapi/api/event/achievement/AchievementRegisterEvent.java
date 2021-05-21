package net.modificationstation.stationapi.api.event.achievement;

import lombok.RequiredArgsConstructor;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.achievement.Achievement;

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
