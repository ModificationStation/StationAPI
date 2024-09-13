package net.modificationstation.stationapi.api.lookup.block;

import net.mine_diver.unsafeevents.event.Cancelable;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.lookup.ApiLookupEvent;

@Cancelable
public class BlockAPILookupEvent extends ApiLookupEvent {

    public final World world;
    public final BlockState blockState;


    public BlockAPILookupEvent(Class<?> apiClass, World world, BlockState blockState) {
        super(apiClass);
        this.world = world;
        this.blockState = blockState;
    }

}
