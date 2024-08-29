package net.modificationstation.stationapi.api.lookup.block;

import net.mine_diver.unsafeevents.event.Cancelable;
import net.minecraft.block.Block;
import net.modificationstation.stationapi.api.lookup.ApiLookupEvent;

@Cancelable
public class BlockAPILookupEvent extends ApiLookupEvent {

    public final Block block;

    public BlockAPILookupEvent(Block block, Class<?> apiClass) {
        super(apiClass);
        this.block = block;
    }

}
