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
        ToolMaterial stone = ToolMaterial.STONE;
        ToolMaterial iron = ToolMaterial.IRON;
        ToolMaterial diamond = ToolMaterial.DIAMOND;
        stone.inheritsFrom(ToolMaterial.WOOD, ToolMaterial.GOLD);
        stone.requiredBlockTag(of("needs_stone_tool"));
        iron.inheritsFrom(ToolMaterial.STONE);
        iron.requiredBlockTag(of("needs_iron_tool"));
        diamond.inheritsFrom(ToolMaterial.IRON);
        diamond.requiredBlockTag(of("needs_diamond_tool"));
    }
}
