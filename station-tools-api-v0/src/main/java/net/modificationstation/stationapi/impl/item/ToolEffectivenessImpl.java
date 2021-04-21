package net.modificationstation.stationapi.impl.item;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.block.BlockMiningLevel;
import net.modificationstation.stationapi.api.event.item.IsItemEffectiveOnBlockEvent;
import net.modificationstation.stationapi.api.event.item.ItemStrengthOnBlockEvent;
import net.modificationstation.stationapi.api.item.tool.OverrideIsEffectiveOn;
import net.modificationstation.stationapi.api.item.tool.ToolLevel;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class ToolEffectivenessImpl {

    @EventListener(priority = ListenerPriority.HIGH)
    private static void isEffective(IsItemEffectiveOnBlockEvent event) {
        ItemBase item = event.itemInstance.getType();
        if (item instanceof ToolLevel) {
            event.effective =
                    ((BlockMiningLevel) event.block).getToolTypes(event.meta, event.itemInstance) != null &&
                            ((BlockMiningLevel) event.block).getToolTypes(event.meta, event.itemInstance).stream().anyMatch(entry -> entry != null && entry.isInstance(event.itemInstance.getType())) &&
                            ((ToolLevel) item).getToolLevel() >= ((BlockMiningLevel) event.block).getBlockLevel(event.meta, event.itemInstance);
            if (item instanceof OverrideIsEffectiveOn)
                event.effective = ((OverrideIsEffectiveOn) item).overrideIsEffectiveOn((ToolLevel) item, event.block, event.meta, event.effective);
        }
    }

    @EventListener(priority = ListenerPriority.HIGH)
    private static void getStrength(ItemStrengthOnBlockEvent event) {
        if (
                event.itemInstance.getType() instanceof ToolLevel &&
                        ((BlockMiningLevel) event.block).getBlockLevel(event.meta, event.itemInstance) <= ((ToolLevel) event.itemInstance.getType()).getToolLevel() &&
                        ((BlockMiningLevel) event.block).getBlockLevel(event.meta, event.itemInstance) != -1 &&
                        ((BlockMiningLevel) event.block).getToolTypes(event.meta, event.itemInstance) != null &&
                        ((BlockMiningLevel) event.block).getToolTypes(event.meta, event.itemInstance).stream().anyMatch((toolLevel) -> toolLevel != null && toolLevel.isInstance(event.itemInstance.getType()))
        )
            event.strength = ((ToolLevel) event.itemInstance.getType()).getMaterial().getMiningSpeed();
    }
}
