package net.modificationstation.stationapi.impl.vanillafix.item.tool;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.item.tool.StationToolMaterial;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;

import static net.modificationstation.stationapi.api.registry.Identifier.of;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public final class VanillaToolFixImpl {

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4)
    private static void fixToolMaterials(ItemRegistryEvent event) {
        StationToolMaterial stone = StationToolMaterial.class.cast(ToolMaterial.field_1689);
        StationToolMaterial iron = StationToolMaterial.class.cast(ToolMaterial.field_1690);
        StationToolMaterial diamond = StationToolMaterial.class.cast(ToolMaterial.field_1691);
        stone.inheritsFrom(ToolMaterial.field_1688, ToolMaterial.field_1692);
        stone.requiredBlockTag(of("needs_stone_tool"));
        iron.inheritsFrom(ToolMaterial.field_1689);
        iron.requiredBlockTag(of("needs_iron_tool"));
        diamond.inheritsFrom(ToolMaterial.field_1690);
        diamond.requiredBlockTag(of("needs_diamond_tool"));
    }
}
