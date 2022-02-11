package net.modificationstation.stationapi.impl.block;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.modificationstation.stationapi.api.block.ReplaceableBlock;
import net.modificationstation.stationapi.api.event.level.LevelEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class ReplaceableBlockImpl {

    @EventListener(priority = ListenerPriority.HIGH)
    private static void isBlockReplaceable(LevelEvent.IsBlockReplaceable event) {
        if (event.replacedBy.id == 0)
            event.replace = true;
        if (event.replacedBy.canPlaceAt(event.level, event.x, event.y, event.z, event.replacedByMeta) && event.block instanceof ReplaceableBlock replaceable)
            event.replace = replaceable.canBeReplaced(event.level, event.x, event.y, event.z, event.replacedBy, event.replacedByMeta);
    }
}