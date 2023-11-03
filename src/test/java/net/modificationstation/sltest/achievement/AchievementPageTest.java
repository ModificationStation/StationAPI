package net.modificationstation.sltest.achievement;

import net.minecraft.block.Block;
import net.modificationstation.stationapi.api.client.gui.screen.menu.AchievementPage;
import net.modificationstation.stationapi.api.registry.Identifier;

import java.util.Random;

public class AchievementPageTest extends AchievementPage {
    public AchievementPageTest(Identifier id) {
        super(id);
    }

    @Override
    public int getBackgroundTexture(Random random, int i, int j, int randomizedRow, int currentTexture) {
        int k = Block.GRAVEL.textureId;
        if (randomizedRow <= 37 && j != 35) {
            if (randomizedRow == 22) {
                k = random.nextInt(2) != 0 ? Block.LAVA.textureId : Block.GRAVEL.textureId;
            } else if (randomizedRow == 10) {
                k = Block.GLOWSTONE.textureId;
            } else if (randomizedRow == 8) {
                k = Block.SOUL_SAND.textureId;
            } else if (randomizedRow > 4) {
                k = Block.NETHERRACK.textureId;
            } else if (randomizedRow > 0) {
                k = Block.BEDROCK.textureId;
            }
        } else {
            k = Block.BEDROCK.textureId;
        }

        return k;
    }
}
