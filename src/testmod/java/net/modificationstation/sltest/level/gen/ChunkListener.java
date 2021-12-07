package net.modificationstation.sltest.level.gen;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.BlockBase;
import net.minecraft.level.biome.Biome;
import net.modificationstation.stationapi.api.event.level.gen.LevelGenEvent;

public class ChunkListener {

    @EventListener
    public void populate(LevelGenEvent.ChunkDecoration event) {
        for (int x = 0; x < 16; x++)
            for (int z = 0; z < 16; z++)
                if ((event.biome == Biome.FOREST || event.biome == Biome.SEASONAL_FOREST) && event.random.nextBoolean())
                    event.level.setTile(event.x + x, 90, event.z + z, BlockBase.DIAMOND_BLOCK.id);
    }
}
