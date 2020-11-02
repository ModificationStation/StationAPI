package net.modificationstation.stationloader.api.common.event.block;

import net.minecraft.block.BlockBase;
import net.modificationstation.stationloader.api.common.event.Event;
import net.modificationstation.stationloader.api.common.factory.EventFactory;

public interface BlockNameSet {

    Event<BlockNameSet> EVENT = EventFactory.INSTANCE.newEvent(BlockNameSet.class, listeners ->
            (blockBase, name) -> {
                for (BlockNameSet event : listeners)
                    name = event.getName(blockBase, name);
                return name;
            });

    String getName(BlockBase blockBase, String name);
}
