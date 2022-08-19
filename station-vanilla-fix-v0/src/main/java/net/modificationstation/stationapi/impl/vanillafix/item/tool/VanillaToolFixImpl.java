package net.modificationstation.stationapi.impl.vanillafix.item.tool;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.template.item.tool.ToolMaterialTemplate;

import static net.modificationstation.stationapi.api.registry.Identifier.of;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public final class VanillaToolFixImpl {

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4)
    private static void fixToolMaterials(ItemRegistryEvent event) {
        ToolMaterialTemplate.cast(ToolMaterial.field_1689).inheritsFrom(ToolMaterial.field_1688, ToolMaterial.field_1692).requiredBlockTag(of("needs_stone_tool"));
        ToolMaterialTemplate.cast(ToolMaterial.field_1690).inheritsFrom(ToolMaterial.field_1689).requiredBlockTag(of("needs_iron_tool"));
        ToolMaterialTemplate.cast(ToolMaterial.field_1691).inheritsFrom(ToolMaterial.field_1690).requiredBlockTag(of("needs_diamond_tool"));
    }
}
