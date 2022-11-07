package net.modificationstation.stationapi.api.vanillafix.level.structure;

import net.minecraft.block.BlockBase;
import net.minecraft.level.Level;
import net.minecraft.level.structure.SpruceTree;
import net.modificationstation.stationapi.api.block.BlockStateHolder;
import net.modificationstation.stationapi.api.world.StationFlatteningWorld;
import net.modificationstation.stationapi.api.vanillafix.block.Blocks;
import net.modificationstation.stationapi.api.vanillafix.block.FixedLeaves;

import java.util.Random;

public class FixedSpruceTree extends SpruceTree {
    @Override
    public boolean generate(Level arg, Random random, int i, int j, int k) {
        int n;
        int n2;
        int n3;
        int n4;
        int n5;
        int n6;
        int n7;
        int n8 = random.nextInt(4) + 6;
        int n9 = 1 + random.nextInt(2);
        int n10 = n8 - n9;
        int n11 = 2 + random.nextInt(2);
        boolean bl = true;
        if (j < 1 || j + n8 + 1 > 128) {
            return false;
        }
        for (n7 = j; n7 <= j + 1 + n8 && bl; ++n7) {
            n6 = n7 - j < n9 ? 0 : n11;
            for (n5 = i - n6; n5 <= i + n6 && bl; ++n5) {
                for (n4 = k - n6; n4 <= k + n6 && bl; ++n4) {
                    if (n7 >= 0 && n7 < 128) {
                        n3 = arg.getTileId(n5, n7, n4);
                        if (n3 == 0 || BlockBase.BY_ID[n3] instanceof FixedLeaves) continue;
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
        n7 = arg.getTileId(i, j - 1, k);
        if (n7 != BlockBase.GRASS.id && n7 != BlockBase.DIRT.id || j >= 128 - n8 - 1) {
            return false;
        }
        arg.setTileInChunk(i, j - 1, k, BlockBase.DIRT.id);
        n6 = random.nextInt(2);
        n5 = 1;
        n4 = 0;
        for (n3 = 0; n3 <= n10; ++n3) {
            n2 = j + n8 - n3;
            for (n = i - n6; n <= i + n6; ++n) {
                int n12 = n - i;
                for (int i2 = k - n6; i2 <= k + n6; ++i2) {
                    int n13 = i2 - k;
                    if (Math.abs(n12) == n6 && Math.abs(n13) == n6 && n6 > 0 || BlockBase.FULL_OPAQUE[arg.getTileId(n, n2, i2)]) continue;
                    ((StationFlatteningWorld) arg).setBlockState(n, n2, i2, ((BlockStateHolder) Blocks.SPRUCE_LEAVES).getDefaultState());
                }
            }
            if (n6 >= n5) {
                n6 = n4;
                n4 = 1;
                if (++n5 <= n11) continue;
                n5 = n11;
                continue;
            }
            ++n6;
        }
        n3 = random.nextInt(3);
        for (n2 = 0; n2 < n8 - n3; ++n2) {
            n = arg.getTileId(i, j + n2, k);
            if (n != 0 && !(BlockBase.BY_ID[n] instanceof FixedLeaves)) continue;
            ((StationFlatteningWorld) arg).setBlockState(i, j + n2, k, ((BlockStateHolder) Blocks.SPRUCE_LOG).getDefaultState());
        }
        return true;
    }
}
