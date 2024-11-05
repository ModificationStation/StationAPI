package net.modificationstation.stationapi.impl.vanillafix.item.tool;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.item.tool.TagToolLevel;
import net.modificationstation.stationapi.api.item.tool.ToolLevel;
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
        TagToolLevel stoneNode = new TagToolLevel(TagKey.of(BlockRegistry.KEY, of("needs_stone_tool")));
        TagToolLevel ironNode = new TagToolLevel(TagKey.of(BlockRegistry.KEY, of("needs_iron_tool")));
        TagToolLevel diamondNode = new TagToolLevel(TagKey.of(BlockRegistry.KEY, of("needs_diamond_tool")));
        ToolLevel.GRAPH.putEdge(stoneNode, ironNode);
        ToolLevel.GRAPH.putEdge(ironNode, diamondNode);
        ToolMaterial.STONE.toolLevel(stoneNode);
        ToolMaterial.IRON.toolLevel(ironNode);
        ToolMaterial.DIAMOND.toolLevel(diamondNode);
    }
}
