package net.modificationstation.stationapi.api.item.tool;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import it.unimi.dsi.fastutil.objects.Reference2BooleanMap;
import it.unimi.dsi.fastutil.objects.Reference2BooleanOpenHashMap;
import net.modificationstation.stationapi.api.block.BlockState;

import java.util.*;

public abstract class ToolLevel {
    protected record TestContext(
            BlockState blockState,
            Optional<Set<ToolLevel>> failed
    ) {}

    private static final Set<ToolLevel> ALL_LEVELS_MUTABLE = Collections.newSetFromMap(new WeakHashMap<>());
    public static final Set<ToolLevel> ALL_LEVELS = Collections.unmodifiableSet(ALL_LEVELS_MUTABLE);
    public static final MutableGraph<ToolLevel> GRAPH = GraphBuilder.directed().build();

    protected final Reference2BooleanMap<BlockState> cache = new Reference2BooleanOpenHashMap<>();

    protected ToolLevel() {
        ALL_LEVELS_MUTABLE.add(this);
    }

    public static boolean isSuitable(ToolLevel toolLevel, BlockState state) {
        if (toolLevel == null) {
            // Tool provides no level - can only mine blocks that don't require a level
            var context = new TestContext(state, Optional.empty());
            return ALL_LEVELS.stream().noneMatch(level -> level.isSuitable(context));
        }
        return toolLevel.cache.computeIfAbsent(state, key -> {
            var failed = new HashSet<ToolLevel>();
            var context = new TestContext(state, Optional.of(Collections.unmodifiableSet(failed)));
            var toolLevels = Set.of(toolLevel);

            // Breadth-first search for a suitable level in the hierarchy
            while (!toolLevels.isEmpty()) {
                var nextSet = new HashSet<ToolLevel>();
                for (var level : toolLevels) {
                    if (level.isSuitable(context)) return true;
                    failed.add(level);
                    nextSet.addAll(GRAPH.predecessors(level));
                }
                toolLevels = nextSet;
            }

            // Hierarchy exhausted, but no exit,
            // So we need to test if the block requires any level,
            // Because if it doesn't, the tool level is suitable.
            var noFailedContext = new TestContext(context.blockState, Optional.empty());
            return ALL_LEVELS.stream().filter(level -> !failed.contains(level)).noneMatch(level -> level.isSuitable(noFailedContext));
        });
    }

    protected abstract boolean isSuitable(TestContext context);
}
