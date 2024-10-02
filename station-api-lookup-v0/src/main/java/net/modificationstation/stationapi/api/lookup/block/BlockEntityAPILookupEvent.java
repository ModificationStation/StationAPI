package net.modificationstation.stationapi.api.lookup.block;

import net.mine_diver.unsafeevents.event.Cancelable;
import net.minecraft.block.entity.BlockEntity;
import net.modificationstation.stationapi.api.lookup.ApiLookupEvent;

@Cancelable
public class BlockEntityAPILookupEvent extends ApiLookupEvent {

    public final BlockEntity blockEntity;

    public BlockEntityAPILookupEvent(BlockEntity blockEntity, Class<?> apiClass) {
        super(apiClass);
        this.blockEntity = blockEntity;
    }
}
