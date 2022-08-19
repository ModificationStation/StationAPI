package net.modificationstation.stationapi.api.vanillafix.level.structure;

import net.minecraft.block.BlockBase;
import net.minecraft.block.Plant;
import net.minecraft.level.Level;
import net.minecraft.level.structure.TallGrass;
import net.modificationstation.stationapi.api.vanillafix.block.FixedLeaves;

import java.util.Random;

public class FixedTallGrass extends TallGrass {
    private final int tileId;
    private final int tileMeta;

    public FixedTallGrass(int i, int j) {
        super(i, j);
        this.tileId = i;
        this.tileMeta = j;
    }

    @Override
    public boolean generate(Level arg, Random random, int i, int j, int k) {
        int n;
        while (((n = arg.getTileId(i, j, k)) == 0 || BlockBase.BY_ID[n] instanceof FixedLeaves) && j > 0) {
            --j;
        }
        for (int i2 = 0; i2 < 128; ++i2) {
            int n2;
            int n3;
            int n4 = i + random.nextInt(8) - random.nextInt(8);
            if (!arg.isAir(n4, n3 = j + random.nextInt(4) - random.nextInt(4), n2 = k + random.nextInt(8) - random.nextInt(8)) || !((Plant)BlockBase.BY_ID[this.tileId]).canGrow(arg, n4, n3, n2)) continue;
            arg.setTileWithMetadata(n4, n3, n2, this.tileId, this.tileMeta);
        }
        return true;
    }
}
