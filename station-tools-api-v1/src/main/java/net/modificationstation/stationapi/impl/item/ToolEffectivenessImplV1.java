package net.modificationstation.stationapi.impl.item;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.modificationstation.stationapi.api.BlockToolLogic;
import net.modificationstation.stationapi.api.event.item.IsItemEffectiveOnBlockEvent;
import net.modificationstation.stationapi.api.event.item.ItemStrengthOnBlockEvent;
import net.modificationstation.stationapi.api.item.IsItemInstanceEffectiveOnMeta;
import net.modificationstation.stationapi.api.item.tool.ToolLevel;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.tags.TagRegistry;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class ToolEffectivenessImplV1 {

    @EventListener(priority = ListenerPriority.HIGH)
    private static void isEffective(IsItemEffectiveOnBlockEvent event) {
        event.effective = event.effective || ((BlockToolLogic) event.block).getToolTagEffectiveness().stream().anyMatch(identifierIntegerBiTuple -> TagRegistry.INSTANCE.get(identifierIntegerBiTuple.one()).map(predicates -> predicates.stream().anyMatch(predicate -> predicate.test(event.itemInstance))).orElse(false));
    }

    @EventListener(priority = ListenerPriority.HIGH)
    private static void getStrength(ItemStrengthOnBlockEvent event) {
        if (event.itemInstance.getType() instanceof ToolLevel && IsItemInstanceEffectiveOnMeta.cast(event.itemInstance).isEffectiveOn(event.block, event.meta)) {
            event.strength = ((ToolLevel) event.itemInstance.getType()).getMaterial().getMiningSpeed();
        }
    }
}
