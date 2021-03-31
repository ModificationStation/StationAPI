package net.modificationstation.sltest.level.gen;

import net.modificationstation.stationapi.api.common.event.EventListener;
import net.modificationstation.stationapi.api.common.event.level.gen.LevelGenEvent;

public class ChunkListener {

    @EventListener
    public void populate(LevelGenEvent.ChunkDecoration event) {
        /*for (int x = 0; x < 16; x++)
            for (int z = 0; z < 16; z++)
                if ((biome == Biome.FOREST || biome == Biome.SEASONAL_FOREST) && random.nextBoolean())
                    level.setTile(i + x, 90, j + z, BlockBase.DIAMOND_BLOCK.id);*/
    } //that's how easy it is
}
