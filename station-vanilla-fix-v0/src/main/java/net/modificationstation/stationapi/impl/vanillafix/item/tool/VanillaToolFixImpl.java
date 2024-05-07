package net.modificationstation.stationapi.impl.vanillafix.item.tool;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.item.tool.MiningLevelManager;
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
        MiningLevelManager.LevelNode stoneNode = new MiningLevelManager.LevelNode(TagKey.of(BlockRegistry.KEY, of("needs_stone_tool")));
        MiningLevelManager.LevelNode ironNode = new MiningLevelManager.LevelNode(TagKey.of(BlockRegistry.KEY, of("needs_iron_tool")));
        MiningLevelManager.LevelNode diamondNode = new MiningLevelManager.LevelNode(TagKey.of(BlockRegistry.KEY, of("needs_diamond_tool")));
        MiningLevelManager.GRAPH.putEdge(stoneNode, ironNode);
        MiningLevelManager.GRAPH.putEdge(ironNode, diamondNode);
        MiningLevelManager.invalidateCache();
        ToolMaterial.STONE.miningLevelNode(stoneNode);
        ToolMaterial.IRON.miningLevelNode(ironNode);
        ToolMaterial.DIAMOND.miningLevelNode(diamondNode);
    }
}
