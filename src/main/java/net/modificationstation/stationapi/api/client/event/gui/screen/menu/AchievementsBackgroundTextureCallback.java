package net.modificationstation.stationapi.api.client.event.gui.screen.menu;

import lombok.AllArgsConstructor;
import net.minecraft.client.gui.screen.menu.Achievements;
import net.modificationstation.stationapi.api.common.event.Event;

import java.util.Random;

@AllArgsConstructor
public class AchievementsBackgroundTextureCallback extends Event {

    public final Achievements achievements;
    public final Random random;
    public final int column, row, randomizedRow;
    public int backgroundTexture;
}
