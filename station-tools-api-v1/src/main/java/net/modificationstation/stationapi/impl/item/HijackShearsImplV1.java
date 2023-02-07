package net.modificationstation.stationapi.impl.item;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.modificationstation.stationapi.api.item.tool.ToolLevel;
import net.modificationstation.stationapi.api.registry.Identifier;

public class HijackShearsImplV1 {

    //TODO: Make this match anything that has shear tool properties. Not sure how to go around this at the moment.
    @EventListener(priority = ListenerPriority.HIGH)
    private static void hijackShearsEvent(ShearsOverrideEvent event) {
        if(!event.overrideShears && event.itemStack.getType() instanceof ToolLevel)
            event.overrideShears = ((ToolLevel) event.itemStack.getType()).getEffectiveBlocks(event.itemStack).id().equals(Identifier.of("mineable/shears"));
    }
}
