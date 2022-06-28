package net.modificationstation.stationapi.impl.block;

import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.DropListProvider;
import net.modificationstation.stationapi.api.event.block.BlockEvent;

import java.util.List;

public class BlockDropListImpl {

    @FunctionalInterface
    public interface DropInvoker {
        void drop(Level level, int x, int y, int z, ItemInstance drop);
    }

    public static boolean drop(
            Level level, int x, int y, int z,
            BlockState state, int meta,
            float chance,
            DropInvoker dropFunc, DropListProvider dropProvider
    ) {
        if (!level.isServerSide) {
            List<ItemInstance> drops = dropProvider.getDropList(level, x, y, z, state, meta);
            if (drops != null) {
                if (
                        !StationAPI.EVENT_BUS.post(
                                BlockEvent.BeforeDrop.builder()
                                        .level(level)
                                        .x(x).y(y).z(z)
                                        .block(state.getBlock()).meta(meta)
                                        .chance(chance)
                                        .build()
                        ).isCanceled()
                ) drops.forEach(drop -> {
                    if (!(level.rand.nextFloat() > chance) && drop.itemId > 0) dropFunc.drop(level, x, y, z, drop);
                });
                return true;
            }
        }
        return false;
    }
}
