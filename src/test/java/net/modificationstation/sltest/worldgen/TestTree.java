package net.modificationstation.sltest.worldgen;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;
import net.modificationstation.stationapi.api.block.BlockState;

import java.util.Random;

public class TestTree extends Feature {
    final BlockState log;
    final BlockState leaves;

    public TestTree(Block log, Block leaves) {
        this(log.getDefaultState(), leaves.getDefaultState());
    }

    public TestTree(BlockState log, BlockState leaves) {
        this.log = log;
        this.leaves = leaves;
    }

    public boolean generate(World level, Random random, int x, int y, int z) {
        int height = random.nextInt(3) + 4;

        for (int py = y - 3 + height; py <= y + height; ++py) {
            int dy = py - (y + height);
            int side = 1 - dy / 2;
            for (int px = x - side; px <= x + side; ++px) {
                int dx = px - x;
                for (int pz = z - side; pz <= z + side; ++pz) {
                    int dz = pz - z;
                    if (
                            Math.abs(dx) == side &&
                                    Math.abs(dz) == side &&
                                    (random.nextInt(2) == 0 || dy == 0) ||
                                    Block.BLOCKS_OPAQUE[level.getBlockId(px, py, pz)]
                    ) continue;
                    level.setBlockState(px, py, pz, leaves);
                }
            }
        }

        for (int i = 0; i < height; ++i) {
            BlockState state = level.getBlockState(x, y + i, z);
            if (!state.isAir() && !state.getMaterial().isReplaceable() && state.isOf(Block.LEAVES)) continue;
            level.setBlockState(x, y + i, z, log);
        }

        return true;
    }
}
