package net.modificationstation.stationapi.impl.item;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.modificationstation.stationapi.api.BlockBaseAccessor;
import net.modificationstation.stationapi.api.event.item.IsItemEffectiveOnBlockEvent;
import net.modificationstation.stationapi.api.event.item.ItemStrengthOnBlockEvent;
import net.modificationstation.stationapi.api.item.tool.ToolLevel;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.tags.TagRegistry;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class ToolEffectivenessImplV1 {

    @EventListener(priority = ListenerPriority.HIGH)
    private static void isEffective(IsItemEffectiveOnBlockEvent event) {
        event.effective = event.effective || ((BlockBaseAccessor) event.block).getToolTagEffectiveness().stream().anyMatch(identifierIntegerBiTuple -> {
            return TagRegistry.INSTANCE.get(identifierIntegerBiTuple.one()).map(predicates -> {
                return predicates.stream().anyMatch(predicate -> {
                    System.out.println(predicate.test(event.itemInstance));
                    return predicate.test(event.itemInstance);
                });
            }).orElse(false);
        });

    }

    @EventListener(priority = ListenerPriority.HIGH)
    private static void getStrength(ItemStrengthOnBlockEvent event) {
        if (event.itemInstance.getType() instanceof ToolLevel) {
            event.strength = ((ToolLevel) event.itemInstance.getType()).getMaterial().getMiningSpeed();
        }
    }
}
