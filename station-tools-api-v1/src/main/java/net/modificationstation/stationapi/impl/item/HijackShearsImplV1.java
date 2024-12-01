package net.modificationstation.stationapi.impl.item;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.item.tool.StationTool;
import net.modificationstation.stationapi.api.util.Identifier;

@EventListener(phase = StationAPI.INTERNAL_PHASE)
public class HijackShearsImplV1 {
    //TODO: Make this match anything that has shear tool properties. Not sure how to go around this at the moment.
    @EventListener
    private static void hijackShearsEvent(ShearsOverrideEvent event) {
        if(!event.overrideShears && event.itemStack.getItem() instanceof StationTool)
            event.overrideShears = ((StationTool) event.itemStack.getItem()).getEffectiveBlocks(event.itemStack).id().equals(Identifier.of("mineable/shears"));
    }
}
