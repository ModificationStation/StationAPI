package net.modificationstation.stationapi.api.item.tool;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import lombok.val;
import net.minecraft.block.Block;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.tag.TagKey;

import java.util.stream.Collectors;

public final class MiningLevelManager {
    public record LevelNode(TagKey<Block> blockTag) {}
    private record CacheKey(LevelNode levelNode, BlockState state) {}

    public static final MutableGraph<LevelNode> GRAPH = GraphBuilder.directed().build();
    private static final Object2BooleanMap<CacheKey> CACHE = new Object2BooleanOpenHashMap<>();

    public static boolean isSuitable(LevelNode levelNode, BlockState state) {
        return CACHE.computeIfAbsent(new CacheKey(levelNode, state), (CacheKey key) -> {
            val nodes = GRAPH.nodes().stream().filter(node -> key.state.isIn(node.blockTag)).collect(Collectors.toSet());
            if (nodes.isEmpty()) return true;
            val pred = GRAPH.predecessors(key.levelNode);
            return nodes.stream().anyMatch(node -> key.levelNode.equals(node) || pred.contains(node));
        });
    }

    public static void invalidateCache() {
        CACHE.clear();
    }
}
