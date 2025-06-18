package net.modificationstation.sltest.level.gen;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.modificationstation.sltest.block.Blocks;
import net.modificationstation.stationapi.api.event.world.gen.WorldGenEvent;

public class ChunkListener {

    @EventListener
    public void populate(WorldGenEvent.ChunkDecoration event) {
//        for (int x = 0; x < 16; x++)
//            for (int z = 0; z < 16; z++)
//                if ((event.biome == Biome.FOREST || event.biome == Biome.SEASONAL_FOREST) && event.random.nextBoolean())
//                    event.level.setTile(event.x + x, 90, event.z + z, BlockBase.DIAMOND_BLOCK.id);
        // Modded leaves and log test
        event.world.setBlock(event.x + 8, 100, event.z + 8, Blocks.MODDED_LOG.get().id);
//        event.world.setBlock(event.x + 1, 100, event.z, Block.LEAVES.id);
//        event.world.setBlock(event.x + 2, 100, event.z, Blocks.MODDED_LEAVES.get().id);
//        event.world.setBlock(event.x + 3, 100, event.z, Block.LEAVES.id);
    }
}
