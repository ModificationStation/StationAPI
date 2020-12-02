package net.modificationstation.stationloader.api.common.event.block;

import net.minecraft.block.BlockBase;
import net.modificationstation.stationloader.api.common.event.SimpleEvent;

public interface BlockNameSet {

    SimpleEvent<BlockNameSet> EVENT = new SimpleEvent<>(BlockNameSet.class, listeners ->
            (blockBase, name) -> {
                for (BlockNameSet event : listeners)
                    name = event.getName(blockBase, name);
                return name;
            });

    String getName(BlockBase blockBase, String name);
}
