package net.modificationstation.stationapi.api.vanillafix.block;

import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.minecraft.stat.Stats;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.level.BlockStateView;
import net.modificationstation.stationapi.api.level.StationFlatteningWorld;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.Properties;
import net.modificationstation.stationapi.api.vanillafix.tag.BlockTags;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class FixedLeaves extends FixedLeavesBase {

    private final Supplier<List<ItemInstance>> drop;
    private static final int[] DISTANCE_MAP = new int[32768];

    public FixedLeaves(Identifier identifier, Supplier<ItemBase> saplingSupplier) {
        super(identifier, Material.LEAVES);
        setDefaultState(getDefaultState().with(Properties.PERSISTENT, true));
        setTicksRandomly(true);
        drop = () -> List.of(new ItemInstance(saplingSupplier.get()));
    }

    @Override
    public void appendProperties(StateManager.Builder<BlockBase, BlockState> builder) {
        builder.add(Properties.PERSISTENT);
    }

    @Override
    public void onBlockRemoved(Level level, int x, int y, int z) {
        int n = 1;
        int n2 = n + 1;
        if (level.method_155(x - n2, y - n2, z - n2, x + n2, y + n2, z + n2))
            for (int i2 = -n; i2 <= n; ++i2) for (int i3 = -n; i3 <= n; ++i3) for (int i4 = -n; i4 <= n; ++i4)
                if (BlockBase.BY_ID[level.getTileId(x + i2, y + i3, z + i4)] instanceof FixedLeaves)
                    ((StationFlatteningWorld) level).setBlockState(x + i2, y + i3, z + i4, ((BlockStateView) level).getBlockState(x + i2, y + i3, z + i4).with(Properties.PERSISTENT, false));
    }

    @Override
    public void onScheduledTick(Level arg, int x, int y, int z, Random random) {
        if (arg.isServerSide) return;
        BlockState state = ((BlockStateView) arg).getBlockState(x, y, z);
        if (!state.get(Properties.PERSISTENT)) {
            int xOff;
            int scanRadius = 4;
            int scanRadiusSafe = scanRadius + 1;
            int mapLength = 32;
            int mapPlaneSize = mapLength * mapLength;
            int mapLengthHalved = mapLength / 2;
            if (arg.method_155(x - scanRadiusSafe, y - scanRadiusSafe, z - scanRadiusSafe, x + scanRadiusSafe, y + scanRadiusSafe, z + scanRadiusSafe)) {
                int n8;
                int zOff;
                int yOff;
                for (xOff = -scanRadius; xOff <= scanRadius; ++xOff) for (yOff = -scanRadius; yOff <= scanRadius; ++yOff) for (zOff = -scanRadius; zOff <= scanRadius; ++zOff) {
                    n8 = arg.getTileId(x + xOff, y + yOff, z + zOff);
                    DISTANCE_MAP[(xOff + mapLengthHalved) * mapPlaneSize + (yOff + mapLengthHalved) * mapLength + (zOff + mapLengthHalved)] = ((StationFlatteningWorld) arg).getBlockState(x + xOff, y + yOff, z + zOff).isIn(BlockTags.LOGS) ? 0 : (BlockBase.BY_ID[n8] instanceof FixedLeaves ? -2 : -1);
                }
                for (xOff = 1; xOff <= 4; ++xOff)
                    for (yOff = -scanRadius; yOff <= scanRadius; ++yOff) for (zOff = -scanRadius; zOff <= scanRadius; ++zOff) for (n8 = -scanRadius; n8 <= scanRadius; ++n8) {
                        if (DISTANCE_MAP[(yOff + mapLengthHalved) * mapPlaneSize + (zOff + mapLengthHalved) * mapLength + (n8 + mapLengthHalved)] != xOff - 1) continue;
                        int index = (yOff + mapLengthHalved - 1) * mapPlaneSize + (zOff + mapLengthHalved) * mapLength + (n8 + mapLengthHalved);
                        if (DISTANCE_MAP[index] == -2) DISTANCE_MAP[index] = xOff;
                        index = (yOff + mapLengthHalved + 1) * mapPlaneSize + (zOff + mapLengthHalved) * mapLength + (n8 + mapLengthHalved);
                        if (DISTANCE_MAP[index] == -2) DISTANCE_MAP[index] = xOff;
                        index = (yOff + mapLengthHalved) * mapPlaneSize + (zOff + mapLengthHalved - 1) * mapLength + (n8 + mapLengthHalved);
                        if (DISTANCE_MAP[index] == -2) DISTANCE_MAP[index] = xOff;
                        index = (yOff + mapLengthHalved) * mapPlaneSize + (zOff + mapLengthHalved + 1) * mapLength + (n8 + mapLengthHalved);
                        if (DISTANCE_MAP[index] == -2) DISTANCE_MAP[index] = xOff;
                        index = (yOff + mapLengthHalved) * mapPlaneSize + (zOff + mapLengthHalved) * mapLength + (n8 + mapLengthHalved - 1);
                        if (DISTANCE_MAP[index] == -2) DISTANCE_MAP[index] = xOff;
                        index = (yOff + mapLengthHalved) * mapPlaneSize + (zOff + mapLengthHalved) * mapLength + (n8 + mapLengthHalved + 1);
                        if (DISTANCE_MAP[index] == -2) DISTANCE_MAP[index] = xOff;
                    }
            }
            if (DISTANCE_MAP[mapLengthHalved * mapPlaneSize + mapLengthHalved * mapLength + mapLengthHalved] >= 0) {
                ((StationFlatteningWorld) arg).setBlockState(x, y, z, state.with(Properties.PERSISTENT, true));
            } else {
                this.dropAndRemove(arg, x, y, z);
            }
        }
    }

    private void dropAndRemove(Level level, int x, int y, int z) {
        drop(level, x, y, z, level.getTileMeta(x, y, z));
        level.setTile(x, y, z, 0);
    }

    @Override
    public List<ItemInstance> getDropList(Level level, int x, int y, int z, BlockState blockState, int meta) {
        return level.rand.nextInt(20) == 0 ? drop.get() : List.of();
    }

    @Override
    public void afterBreak(Level arg, PlayerBase arg2, int i, int j, int k, int l) {
        if (!arg.isServerSide && arg2.getHeldItem() != null && arg2.getHeldItem().itemId == ItemBase.shears.id) {
            arg2.increaseStat(Stats.mineBlock[this.id], 1);
            drop(arg, i, j, k, new ItemInstance(this));
        } else super.afterBreak(arg, arg2, i, j, k, l);
    }


}
