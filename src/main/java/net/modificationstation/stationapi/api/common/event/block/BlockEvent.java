package net.modificationstation.stationapi.api.common.event.block;

import lombok.RequiredArgsConstructor;
import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.common.event.Event;

@RequiredArgsConstructor
public class BlockEvent extends Event {

    public final BlockBase block;
}
