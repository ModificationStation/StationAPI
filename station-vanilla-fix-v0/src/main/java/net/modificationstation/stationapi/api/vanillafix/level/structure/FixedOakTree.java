package net.modificationstation.stationapi.api.vanillafix.level.structure;

import net.minecraft.block.BlockBase;
import net.minecraft.level.Level;
import net.minecraft.level.structure.OakTree;
import net.modificationstation.stationapi.api.block.BlockStateHolder;
import net.modificationstation.stationapi.api.level.StationFlatteningLevel;
import net.modificationstation.stationapi.api.vanillafix.block.Blocks;
import net.modificationstation.stationapi.api.vanillafix.block.FixedLeaves;

import java.util.Random;

public class FixedOakTree extends OakTree {
    @Override
    public boolean generate(Level arg, Random random, int i, int j, int k) {
        int n;
        int n2;
        int n3;
        int n4;
        int n5;
        int n6 = random.nextInt(3) + 4;
        boolean bl = true;
        if (j < 1 || j + n6 + 1 > 128) {
            return false;
        }
        for (n5 = j; n5 <= j + 1 + n6; ++n5) {
            n4 = 1;
            if (n5 == j) {
                n4 = 0;
            }
            if (n5 >= j + 1 + n6 - 2) {
                n4 = 2;
            }
            for (n3 = i - n4; n3 <= i + n4 && bl; ++n3) {
                for (n2 = k - n4; n2 <= k + n4 && bl; ++n2) {
                    if (n5 >= 0 && n5 < 128) {
                        n = arg.getTileId(n3, n5, n2);
                        if (n == 0 || BlockBase.BY_ID[n] instanceof FixedLeaves) continue;
                        bl = false;
                        continue;
                    }
                    bl = false;
                }
            }
        }
        if (!bl) {
            return false;
        }
        n5 = arg.getTileId(i, j - 1, k);
        if (n5 != BlockBase.GRASS.id && n5 != BlockBase.DIRT.id || j >= 128 - n6 - 1) {
            return false;
        }
        arg.setTileInChunk(i, j - 1, k, BlockBase.DIRT.id);
        for (n4 = j - 3 + n6; n4 <= j + n6; ++n4) {
            n3 = n4 - (j + n6);
            n2 = 1 - n3 / 2;
            for (n = i - n2; n <= i + n2; ++n) {
                int n7 = n - i;
                for (int i2 = k - n2; i2 <= k + n2; ++i2) {
                    int n8 = i2 - k;
                    if (Math.abs(n7) == n2 && Math.abs(n8) == n2 && (random.nextInt(2) == 0 || n3 == 0) || BlockBase.FULL_OPAQUE[arg.getTileId(n, n4, i2)]) continue;
                    ((StationFlatteningLevel) arg).setBlockState(n, n4, i2, ((BlockStateHolder) Blocks.OAK_LEAVES).getDefaultState());
                }
            }
        }
        for (n4 = 0; n4 < n6; ++n4) {
            n3 = arg.getTileId(i, j + n4, k);
            if (n3 != 0 && !(BlockBase.BY_ID[n3] instanceof FixedLeaves)) continue;
            ((StationFlatteningLevel) arg).setBlockState(i, j + n4, k, ((BlockStateHolder) Blocks.OAK_LOG).getDefaultState());
        }
        return true;
    }
}
