package net.modificationstation.sltest.achievement;

import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.client.gui.screen.menu.AchievementPage;
import net.modificationstation.stationapi.api.registry.ModID;

import java.util.*;

public class AchievementPageTest extends AchievementPage {

    public AchievementPageTest(ModID modID, String name) {
        super(modID, name);
    }

    @Override
    public int getBackgroundTexture(Random random, int i, int j, int randomizedRow, int currentTexture) {
        int k = BlockBase.GRAVEL.texture;
        if (randomizedRow <= 37 && j != 35) {
            if (randomizedRow == 22) {
                k = random.nextInt(2) != 0 ? BlockBase.STILL_LAVA.texture : BlockBase.GRAVEL.texture;
            } else if (randomizedRow == 10) {
                k = BlockBase.GLOWSTONE.texture;
            } else if (randomizedRow == 8) {
                k = BlockBase.SOUL_SAND.texture;
            } else if (randomizedRow > 4) {
                k = BlockBase.NETHERRACK.texture;
            } else if (randomizedRow > 0) {
                k = BlockBase.BEDROCK.texture;
            }
        } else {
            k = BlockBase.BEDROCK.texture;
        }

        return k;
    }
}
