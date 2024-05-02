package net.modificationstation.stationapi.impl.vanillafix.item.tool;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.tag.TagKey;

import static net.modificationstation.stationapi.api.util.Identifier.of;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public final class VanillaToolFixImpl {
    @EventListener
    private static void fixToolMaterials(ItemRegistryEvent event) {
        ToolMaterial stone = ToolMaterial.STONE;
        ToolMaterial iron = ToolMaterial.IRON;
        ToolMaterial diamond = ToolMaterial.DIAMOND;
        stone.miningLevelTag(TagKey.of(BlockRegistry.KEY, of("needs_stone_tool")));
        iron.miningLevelTag(TagKey.of(BlockRegistry.KEY, of("needs_iron_tool")));
        diamond.miningLevelTag(TagKey.of(BlockRegistry.KEY, of("needs_diamond_tool")));
    }
}
