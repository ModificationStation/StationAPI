package net.modificationstation.stationloader.api.common.event.item.tool;

import net.minecraft.block.BlockBase;
import net.modificationstation.stationloader.api.common.event.Event;
import net.modificationstation.stationloader.api.common.factory.EventFactory;
import net.modificationstation.stationloader.api.common.item.tool.ToolLevel;

import java.util.concurrent.atomic.AtomicBoolean;

public interface IsEffectiveOn {

    Event<IsEffectiveOn> EVENT = EventFactory.INSTANCE.newEvent(IsEffectiveOn.class, listeners ->
            (toolBase, arg, meta, effective) -> {
                for (IsEffectiveOn listener : listeners)
                    listener.isEffectiveOn(toolBase, arg, meta, effective);
            });

    void isEffectiveOn(ToolLevel toolLevel, BlockBase arg, int meta, AtomicBoolean effective);
}
