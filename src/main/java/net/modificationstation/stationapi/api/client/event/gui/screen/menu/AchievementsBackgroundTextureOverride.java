package net.modificationstation.stationapi.api.client.event.gui.screen.menu;

import net.minecraft.client.gui.screen.menu.Achievements;

import java.util.Random;

public class AchievementsBackgroundTextureOverride extends AchievementsEvent {

    public final Random random;
    public final int column, row, randomizedRow;
    public int backgroundTexture;

    public AchievementsBackgroundTextureOverride(Achievements achievements, Random random, int column, int row, int randomizedRow, int backgroundTexture) {
        super(achievements);
        this.random = random;
        this.column = column;
        this.row = row;
        this.randomizedRow = randomizedRow;
        this.backgroundTexture = backgroundTexture;
    }
}
