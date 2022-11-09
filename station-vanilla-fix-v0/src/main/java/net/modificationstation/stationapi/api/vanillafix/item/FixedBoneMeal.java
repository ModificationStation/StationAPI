package net.modificationstation.stationapi.api.vanillafix.item;

import net.minecraft.block.BlockBase;
import net.minecraft.block.Crops;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.minecraft.util.maths.TilePos;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.vanillafix.block.FixedSapling;
import net.modificationstation.stationapi.api.vanillafix.util.DyeColor;
import net.modificationstation.stationapi.api.world.BlockStateView;

public class FixedBoneMeal extends FixedDye {

    public FixedBoneMeal(Identifier identifier, DyeColor color) {
        super(identifier, color);
    }

    @Override
    public boolean useOnTile(ItemInstance arg, PlayerBase arg2, Level arg3, int i, int j, int k, int l) {
        int n = arg3.getTileId(i, j, k);
        if (BlockBase.BY_ID[n] instanceof FixedSapling sapling) {
            if (!arg3.isServerSide) {
                sapling.generate(arg3, new TilePos(i, j, k), ((BlockStateView) arg3).getBlockState(i, j, k), arg3.rand);
                --arg.count;
            }
            return true;
        }
        if (n == BlockBase.CROPS.id) {
            if (!arg3.isServerSide) {
                ((Crops)BlockBase.CROPS).growCropInstantly(arg3, i, j, k);
                --arg.count;
            }
            return true;
        }
        if (n == BlockBase.GRASS.id) {
            if (!arg3.isServerSide) {
                --arg.count;
                block0: for (int i2 = 0; i2 < 128; ++i2) {
                    int n2 = i;
                    int n3 = j + 1;
                    int n4 = k;
                    for (int i3 = 0; i3 < i2 / 16; ++i3) {
                        if (arg3.getTileId(n2 += rand.nextInt(3) - 1, (n3 += (rand.nextInt(3) - 1) * rand.nextInt(3) / 2) - 1, n4 += rand.nextInt(3) - 1) != BlockBase.GRASS.id || arg3.canSuffocate(n2, n3, n4)) continue block0;
                    }
                    if (arg3.getTileId(n2, n3, n4) != 0) continue;
                    if (rand.nextInt(10) != 0) {
                        arg3.placeBlockWithMetaData(n2, n3, n4, BlockBase.TALLGRASS.id, 1);
                        continue;
                    }
                    if (rand.nextInt(3) != 0) {
                        arg3.setTile(n2, n3, n4, BlockBase.DANDELION.id);
                        continue;
                    }
                    arg3.setTile(n2, n3, n4, BlockBase.ROSE.id);
                }
            }
            return true;
        }
        return false;
    }
}
