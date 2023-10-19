package net.modificationstation.sltest.worldgen;

import net.minecraft.block.BlockBase;
import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;
import net.modificationstation.stationapi.api.block.BlockState;

import java.util.Random;

public class TestTree extends Structure {
    final BlockState log;
    final BlockState leaves;

    public TestTree(BlockBase log, BlockBase leaves) {
        this(log.getDefaultState(), leaves.getDefaultState());
    }

    public TestTree(BlockState log, BlockState leaves) {
        this.log = log;
        this.leaves = leaves;
    }

    public boolean generate(Level level, Random random, int x, int y, int z) {
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
                                    BlockBase.FULL_OPAQUE[level.getTileId(px, py, pz)]
                    ) continue;
                    level.setBlockState(px, py, pz, leaves);
                }
            }
        }

        for (int i = 0; i < height; ++i) {
            BlockState state = level.getBlockState(x, y + i, z);
            if (!state.isAir() && !state.getMaterial().isReplaceable() && state.isOf(BlockBase.LEAVES)) continue;
            level.setBlockState(x, y + i, z, log);
        }

        return true;
    }
}
