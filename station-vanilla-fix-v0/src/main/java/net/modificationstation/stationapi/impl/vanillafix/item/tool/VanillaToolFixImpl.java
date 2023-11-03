package net.modificationstation.stationapi.impl.vanillafix.item.tool;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;

import static net.modificationstation.stationapi.api.registry.Identifier.of;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public final class VanillaToolFixImpl {

    @EventListener
    private static void fixToolMaterials(ItemRegistryEvent event) {
        ToolMaterial stone = ToolMaterial.field_1689;
        ToolMaterial iron = ToolMaterial.field_1690;
        ToolMaterial diamond = ToolMaterial.DIAMOND;
        stone.inheritsFrom(ToolMaterial.field_1688, ToolMaterial.field_1692);
        stone.requiredBlockTag(of("needs_stone_tool"));
        iron.inheritsFrom(ToolMaterial.field_1689);
        iron.requiredBlockTag(of("needs_iron_tool"));
        diamond.inheritsFrom(ToolMaterial.field_1690);
        diamond.requiredBlockTag(of("needs_diamond_tool"));
    }
}
