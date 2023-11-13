package net.modificationstation.stationapi.impl.block;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.DropListProvider;
import net.modificationstation.stationapi.api.event.block.BlockEvent;

import java.util.List;

public class BlockDropListImpl {

    @FunctionalInterface
    public interface DropInvoker {
        void drop(World world, int x, int y, int z, ItemStack drop);
    }

    public static boolean drop(
            World world, int x, int y, int z,
            BlockState state, int meta,
            float chance,
            DropInvoker dropFunc, DropListProvider dropProvider
    ) {
        if (!world.isRemote) {
            List<ItemStack> drops = dropProvider.getDropList(world, x, y, z, state, meta);
            if (drops != null) {
                if (
                        !StationAPI.EVENT_BUS.post(
                                BlockEvent.BeforeDrop.builder()
                                        .world(world)
                                        .x(x).y(y).z(z)
                                        .block(state.getBlock()).meta(meta)
                                        .chance(chance)
                                        .build()
                        ).isCanceled()
                ) drops.forEach(drop -> {
                    if (!(world.field_214.nextFloat() > chance) && drop.itemId > 0) dropFunc.drop(world, x, y, z, drop);
                });
                return true;
            }
        }
        return false;
    }
}
