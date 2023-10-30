package net.modificationstation.stationapi.api.worldgen.surface;

import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.math.Direction.AxisDirection;
import net.modificationstation.stationapi.api.worldgen.surface.condition.*;

import java.util.ArrayList;
import java.util.List;

public class SurfaceBuilder {
    private static final ThreadLocal<SurfaceBuilder> INSTANCES = ThreadLocal.withInitial(SurfaceBuilder::new);
    private final List<ConditionInfo> conditions = new ArrayList<>();
    private SurfaceRule rule;

    private SurfaceBuilder() {}

    /**
     * Start surface building with any starting rule
     */
    public static SurfaceBuilder start(SurfaceRule rule) {
        SurfaceBuilder instance = INSTANCES.get();
        instance.rule = rule;
        return instance;
    }

    /**
     * Start surface building with {@link StateSurfaceRule}
     */
    public static SurfaceBuilder start(BlockState state) {
        return start(new StateSurfaceRule(state));
    }

    /**
     * Start surface building with {@link StateSurfaceRule} with default blockstate
     */
    public static SurfaceBuilder start(BlockBase block) {
        return start(new StateSurfaceRule(block.getDefaultState()));
    }

    /**
     * Add any condition to rule with any priority (lower values = higher priority)
     */
    public SurfaceBuilder condition(SurfaceCondition condition, int priority) {
        conditions.add(new ConditionInfo(condition, priority));
        return this;
    }

    /**
     * Add ground condition - surface will be generated as ground (under air or other no-solid blocks)
     */
    public SurfaceBuilder ground(int depth) {
        return condition(new DepthSurfaceCondition(depth, AxisDirection.NEGATIVE), 1);
    }

    /**
     * Add ground condition - surface will be generated as ground (under air or other no-solid blocks)
     */
    public SurfaceBuilder ground(int minDepth, int maxDepth) {
        return condition(new DepthSurfaceCondition(minDepth, maxDepth, AxisDirection.NEGATIVE), 2);
    }

    /**
     * Add ceiling condition - surface will be generated on ceiling (above air or other no-solid blocks)
     */
    public SurfaceBuilder ceiling(int depth) {
        return condition(new DepthSurfaceCondition(depth, AxisDirection.POSITIVE), 1);
    }

    /**
     * Add ceiling condition - surface will be generated on ceiling (above air or other no-solid blocks)
     */
    public SurfaceBuilder ceiling(int minDepth, int maxDepth) {
        return condition(new DepthSurfaceCondition(minDepth, maxDepth, AxisDirection.POSITIVE), 2);
    }

    /**
     * Add slope condition - surface will be generated if slope angle will be greater than specified angle.
     * Angle is in degrees
     */
    public SurfaceBuilder slope(float angle) {
        return condition(new SlopeSurfaceCondition(angle, true, true), 3);
    }

    /**
     * Add slope condition - surface will be generated if slope angle will be greater or lower than specified angle.
     * Angle is in degrees
     */
    public SurfaceBuilder slope(float angle, boolean greater) {
        return condition(new SlopeSurfaceCondition(angle, true, greater), 3);
    }

    /**
     * Add slope condition - surface will be generated if slope angle will be greater or lower than specified angle.
     * Angle can be in degrees or in radians
     */
    public SurfaceBuilder slope(float angle, boolean degrees, boolean greater) {
        return condition(new SlopeSurfaceCondition(angle, degrees, greater), 3);
    }

    /**
     * Add state condition - surface will replace only this blockstate
     */
    public SurfaceBuilder replace(BlockState filter) {
        return condition(new StateSurfaceCondition(filter), 4);
    }

    /**
     * Add state condition - surface will replace only this block
     */
    public SurfaceBuilder replace(BlockBase filter) {
        return condition(new BlockSurfaceCondition(filter), 4);
    }

    /**
     * Add state condition - surface will replace only blocks with specified tag
     */
    public SurfaceBuilder replace(TagKey<BlockBase> filter) {
        return condition(new TagSurfaceCondition(filter), 4);
    }

    /**
     * Add range condition - surface will be applied only in specific Y range
     */
    public SurfaceBuilder range(int minY, int maxY) {
        return condition(new HeightSurfaceCondition(minY, maxY), 0);
    }

    /**
     * Finish building process
     */
    public SurfaceRule build() {
        conditions.stream().sorted().forEach(info -> rule.addCondition(info.condition));
        conditions.clear();
        return rule;
    }

    private record ConditionInfo(SurfaceCondition condition, int priority) implements Comparable<ConditionInfo> {
        @Override
        public int compareTo(ConditionInfo info) {
            return Integer.compare(priority, info.priority);
        }
    }
}
