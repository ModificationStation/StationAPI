package net.modificationstation.stationapi.api.vanillafix.level.structure;

import net.minecraft.block.BlockBase;
import net.minecraft.level.Level;
import net.minecraft.level.structure.Deadbush;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.world.StationFlatteningWorld;
import net.modificationstation.stationapi.api.vanillafix.block.FixedLeaves;

import java.util.Random;

public class FixedDeadbush extends Deadbush {

    private final BlockState state;

    public FixedDeadbush(BlockState state) {
        super(state.getBlock().id);
        this.state = state;
    }

    @Override
    public boolean generate(Level arg, Random random, int i, int j, int k) {
        int n;
        while (((n = arg.getTileId(i, j, k)) == 0 || BlockBase.BY_ID[n] instanceof FixedLeaves) && j > 0) {
            --j;
        }
        for (int i2 = 0; i2 < 4; ++i2) {
            int n4 = i + random.nextInt(8) - random.nextInt(8);
            int n2;
            int n3;
            if (!arg.isAir(n4, n3 = j + random.nextInt(4) - random.nextInt(4), n2 = k + random.nextInt(8) - random.nextInt(8)) || !state.getBlock().canGrow(arg, n4, n3, n2)) continue;
            ((StationFlatteningWorld) arg).setBlockState(n4, n3, n2, state);
        }
        return true;
    }
}
