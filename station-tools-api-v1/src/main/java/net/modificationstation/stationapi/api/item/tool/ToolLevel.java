package net.modificationstation.stationapi.api.item.tool;

import com.google.common.collect.ImmutableSet;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import it.unimi.dsi.fastutil.objects.Reference2BooleanMap;
import it.unimi.dsi.fastutil.objects.Reference2BooleanOpenHashMap;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.Util;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.modificationstation.stationapi.api.util.Identifier.of;

public abstract class ToolLevel {
    /**
     * @param blockState the block state the tool level is being tested against.
     * @param failed a collection of successor tool levels (as defined in {@link #GRAPH}) already tested in a run of
     *               {@link #isSuitable(ToolLevel, BlockState)} that ended up being not suitable.
     *               <p>Each index corresponds to a level of the search down to the current point,
     *               0 being the tool level assigned to the tool item itself, and size-1 being immediate successors
     *               (and siblings of successors) of the current level.</p>
     *               <p>Current level isn't reflected in the collection, as its iteration isn't deterministic.</p>
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
     *               thus if a tool level performs a test involving the absent collection,
     *               it must assume the collection matches the conditions, as to not hide a case where the level
     *               can actually be suitable.
     *               </li>
     *               <li>
     *               Collection is empty - this means that this tool level is the first one to be tested,
     *               thus it's also the one that the tool item was assigned to.
     *               Shouldn't normally require any special handling.
     *               </li>
     *               </ol>
     */
    protected record TestContext(
            BlockState blockState,
            Optional<List<Set<ToolLevel>>> failed
    ) {}

    private static final Set<ToolLevel> ALL_LEVELS_MUTABLE = Collections.newSetFromMap(new WeakHashMap<>());
    public static final Set<ToolLevel> ALL_LEVELS = Collections.unmodifiableSet(ALL_LEVELS_MUTABLE);
    public static final MutableGraph<ToolLevel> GRAPH = GraphBuilder.directed().build();
    private static final List<ToolLevel> NUMERIC_LEVELS = Util.make(new ArrayList<>(), list -> {
        var stone = new TagToolLevel(TagKey.of(BlockRegistry.KEY, of("needs_stone_tool")));
        var iron = new TagToolLevel(TagKey.of(BlockRegistry.KEY, of("needs_iron_tool")));
        var diamond = new TagToolLevel(TagKey.of(BlockRegistry.KEY, of("needs_diamond_tool")));
        list.add(null);
        list.add(stone);
        list.add(iron);
        list.add(diamond);
        GRAPH.putEdge(stone, iron);
        GRAPH.putEdge(iron, diamond);
    });

    public static ToolLevel getNumeric(int level) {
        if (level >= NUMERIC_LEVELS.size())
            for (int i = NUMERIC_LEVELS.size(); i < level + 1; i++) {
                var toolLevel = new TagToolLevel(TagKey.of(BlockRegistry.KEY, of("needs_tool_level_" + i)));
                NUMERIC_LEVELS.add(toolLevel);
                GRAPH.putEdge(NUMERIC_LEVELS.get(i - 1), toolLevel);
            }
        return NUMERIC_LEVELS.get(level);
    }

    public static boolean isSuitable(ToolLevel toolLevel, BlockState state) {
        if (toolLevel == null) {
            // Tool provides no level - can only mine blocks that don't require a level
            var context = new TestContext(state, Optional.empty());
            return ALL_LEVELS.stream().noneMatch(level -> level.isSuitable(context));
        }
        return toolLevel.cache.computeIfAbsent(state, key -> {
            var failed = new ArrayList<Set<ToolLevel>>();
            var context = new TestContext(state, Optional.of(Collections.unmodifiableList(failed)));
            var toolLevels = new HashSet<ToolLevel>();
            toolLevels.add(toolLevel);

            // Breadth-first search for a suitable level in the hierarchy
            while (!toolLevels.isEmpty()) {
                // Add immediate siblings of levels that allow them
                toolLevels.addAll(toolLevels.stream()
                        .filter(level -> level.equivalentToImmediateSiblings)
                        .flatMap(level -> Stream.concat(
                                GRAPH.predecessors(level).stream().map(GRAPH::successors).flatMap(Set::stream),
                                GRAPH.successors(level).stream().map(GRAPH::predecessors).flatMap(Set::stream)
                        )).collect(Collectors.toSet())
                );

                var failedBuilder = ImmutableSet.<ToolLevel>builder();
                var nextSet = new HashSet<ToolLevel>();
                for (var level : toolLevels) {
                    if (level.isSuitable(context)) return true;
                    failedBuilder.add(level);
                    nextSet.addAll(GRAPH.predecessors(level));
                }
                failed.add(failedBuilder.build());
                toolLevels = nextSet;

                // Check for loops
                var reoccurred = failed.stream().flatMap(Set::stream).filter(toolLevels::contains).collect(Collectors.toSet());
                if (!reoccurred.isEmpty()) throw new IllegalStateException(
                        "Infinite loop detected in tool levels graph. Tested level - %s, search depth - %d, reoccurred levels - %s"
                                .formatted(toolLevel, failed.size(), reoccurred)
                );
            }

            // Hierarchy exhausted, but no exit,
            // So we need to test if the block requires any level,
            // Because if it doesn't, the tool level is suitable.
            var noFailedContext = new TestContext(context.blockState, Optional.empty());
            var failedFlat = failed.stream().flatMap(Set::stream).collect(Collectors.toSet());
            return ALL_LEVELS.stream().filter(Predicate.not(failedFlat::contains)).noneMatch(level -> level.isSuitable(noFailedContext));
        });
    }

    protected final Reference2BooleanMap<BlockState> cache = new Reference2BooleanOpenHashMap<>();
    protected boolean equivalentToImmediateSiblings;

    protected ToolLevel() {
        ALL_LEVELS_MUTABLE.add(this);
    }

    public ToolLevel equivalentToImmediateSiblings() {
        equivalentToImmediateSiblings = true;
        return this;
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
