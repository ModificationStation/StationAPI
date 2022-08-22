package net.modificationstation.stationapi.api.vanillafix.level.structure;

import net.minecraft.block.BlockBase;
import net.minecraft.level.Level;
import net.minecraft.level.structure.PineTree;
import net.modificationstation.stationapi.api.block.BlockStateHolder;
import net.modificationstation.stationapi.api.level.StationFlatteningWorld;
import net.modificationstation.stationapi.api.vanillafix.block.Blocks;
import net.modificationstation.stationapi.api.vanillafix.block.FixedLeaves;

import java.util.Random;

public class FixedPineTree extends PineTree {
    @Override
    public boolean generate(Level arg, Random random, int i, int j, int k) {
        int n;
        int n2;
        int n3;
        int n4;
        int n5;
        int n6 = random.nextInt(5) + 7;
        int n7 = n6 - random.nextInt(2) - 3;
        int n8 = n6 - n7;
        int n9 = 1 + random.nextInt(n8 + 1);
        boolean bl = true;
        if (j < 1 || j + n6 + 1 > 128) {
            return false;
        }
        for (n5 = j; n5 <= j + 1 + n6 && bl; ++n5) {
            n4 = n5 - j < n7 ? 0 : n9;
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
        n4 = 0;
        for (n3 = j + n6; n3 >= j + n7; --n3) {
            for (n2 = i - n4; n2 <= i + n4; ++n2) {
                n = n2 - i;
                for (int i2 = k - n4; i2 <= k + n4; ++i2) {
                    int n10 = i2 - k;
                    if (Math.abs(n) == n4 && Math.abs(n10) == n4 && n4 > 0 || BlockBase.FULL_OPAQUE[arg.getTileId(n2, n3, i2)]) continue;
                    ((StationFlatteningWorld) arg).setBlockState(n2, n3, i2, ((BlockStateHolder) Blocks.SPRUCE_LEAVES).getDefaultState());
                }
            }
            if (n4 >= 1 && n3 == j + n7 + 1) {
                --n4;
                continue;
            }
            if (n4 >= n9) continue;
            ++n4;
        }
        for (n3 = 0; n3 < n6 - 1; ++n3) {
            n2 = arg.getTileId(i, j + n3, k);
            if (n2 != 0 && !(BlockBase.BY_ID[n2] instanceof FixedLeaves)) continue;
            ((StationFlatteningWorld) arg).setBlockState(i, j + n3, k, ((BlockStateHolder) Blocks.SPRUCE_LOG).getDefaultState());
        }
        return true;
    }
}
