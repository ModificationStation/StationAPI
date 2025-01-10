package net.modificationstation.stationapi.impl.dispenser;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.Listener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.dispenser.DispenseEvent;
import net.modificationstation.stationapi.api.item.CustomDispenseBehavior;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;

import java.lang.invoke.MethodHandles;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public class CustomDispenseBehaviorImpl {
    static {
        Listener.registerLookup(MethodHandles.lookup());
    }

    @EventListener
    private static void implementCustomDispenseBehaviorInterface(DispenseEvent event) {
        if (event.context.itemStack != null) {
            if (event.context.itemStack.getItem() instanceof CustomDispenseBehavior behavior) {
                behavior.dispense(event.context);
                event.cancel();
            }
        }
    }
}
