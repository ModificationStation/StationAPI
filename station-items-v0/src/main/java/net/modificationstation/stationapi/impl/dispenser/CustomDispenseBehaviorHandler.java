package net.modificationstation.stationapi.impl.dispenser;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.dispenser.DispenseEvent;
import net.modificationstation.stationapi.api.item.CustomDispenseBehavior;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public class CustomDispenseBehaviorHandler {
    @EventListener
    public static void implementCustomDispenseBehaviorInterface(DispenseEvent event) {
        if (event.itemDispenseContext.itemStack != null) {
            if (event.itemDispenseContext.itemStack.getItem() instanceof CustomDispenseBehavior behavior) {
                behavior.dispense(event.itemDispenseContext);
                event.cancel();
            }
        }
    }
}
