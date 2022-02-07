package net.modificationstation.stationapi.impl.item;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.tags.TagRegistry;

public class HijackShearsImplV1 {

    @EventListener(priority = ListenerPriority.HIGH)
    private static void hijackShearsEvent(ShearsOverrideEvent event) {
        event.overrideShears = !event.overrideShears && TagRegistry.INSTANCE.tagMatches(Identifier.of("tools/shears"), event.itemInstance);
    }
}
