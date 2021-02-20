package net.modificationstation.stationapi.api.client.event.gui.screen.menu;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.gui.screen.menu.Achievements;
import net.modificationstation.stationapi.api.common.event.Event;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AchievementsEvent extends Event {

    public final Achievements achievementsScreen;
}
