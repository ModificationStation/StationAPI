package net.modificationstation.stationapi.api.item.tool;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import it.unimi.dsi.fastutil.objects.Reference2BooleanMap;
import it.unimi.dsi.fastutil.objects.Reference2BooleanOpenHashMap;
import net.modificationstation.stationapi.api.block.BlockState;

import java.util.*;

public abstract class ToolLevel {
    /**
     * @param blockState the block state the tool level is being tested against.
     * @param failed a set of successor tool levels (as defined in {@link #GRAPH}) already tested in a run of
     *               {@link #isSuitable(ToolLevel, BlockState)} that ended up being not suitable.
     *               <p>Can be used for complex context-aware suitability testing - e.g. limiting suitability
     *               to a fixed set of levels in the graph instead of making all succeeding levels also suitable.</p>
     *               <p>Special cases:</p>
     *               <ol>
     *               <li>
     *               Optional is empty - this means that the level is being tested in an unordered manner,
     *               thus keeping track of failed levels would yield no useful context. For example, this is done
     *               in {@link #isSuitable(ToolLevel, BlockState)} when the initial level's hierarchy was exhausted,
     *               but no match was found. In this case we need to iterate through {@link #ALL_LEVELS}
     *               in order to determine if the tested block state requires any level at all,
     *               thus if a tool level performs a test involving the absent set,
     *               it must assume the set matches the conditions, as to not hide a case where the level
     *               can actually be suitable.
     *               </li>
     *               <li>
     *               Set is empty - this means that this tool level is the first one to be tested,
     *               thus it's also the one that the tool item was assigned to.
     *               Shouldn't normally require any special handling.
     *               </li>
     *               </ol>
     */
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

    /**
     * @param context the record containing available parameters for testing this tool level's suitability.
     * @return whether this and only this tool level is suitable in the given context.
     * Unless you know what you're doing, preceding tool levels must NOT be taken into account,
     * as they will be tested directly, with their own implementation and better context,
     * if this specific level isn't suitable.
     */
    protected abstract boolean isSuitable(TestContext context);
}
