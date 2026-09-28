package net.modificationstation.stationapi.api.tag;

import net.modificationstation.stationapi.api.util.context.Condition;
import net.modificationstation.stationapi.api.util.context.Context;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Composition helpers for the predicates that decide whether a value belongs to a tag
 * under a given {@link Context}.
 *
 * <p>A tag's membership is a map of value to predicate rather than a flat set, because a tag
 * entry may carry conditions that can only be evaluated at query time. {@link #ALWAYS} is a
 * canonical singleton, so unconditional membership stays detectable by identity even after
 * composition. That lets a removal of an unconditional entry drop the value outright instead
 * of building up an ever growing predicate chain.
 */
public final class TagConditions {
    /**
     * Membership that holds in every context.
     */
    public static final Predicate<Context> ALWAYS = context -> true;

    private TagConditions() {}

    /**
     * {@return whether the given predicate holds unconditionally}
     */
    public static boolean isAlways(Predicate<Context> predicate) {
        return predicate == ALWAYS;
    }

    /**
     * {@return a predicate that holds when every one of the given conditions holds}
     */
    public static Predicate<Context> of(Collection<Condition<?>> conditions) {
        if (conditions.isEmpty()) return ALWAYS;
        List<Condition<?>> copy = List.copyOf(conditions);
        return context -> {
            for (Condition<?> condition : copy) if (!condition.test(context)) return false;
            return true;
        };
    }

    /**
     * {@return a predicate that holds when both of the given predicates hold}
     */
    public static Predicate<Context> and(Predicate<Context> left, Predicate<Context> right) {
        if (left == ALWAYS) return right;
        if (right == ALWAYS) return left;
        return left.and(right);
    }

    /**
     * {@return a predicate that holds when either of the given predicates holds}
     */
    public static Predicate<Context> or(Predicate<Context> left, Predicate<Context> right) {
        if (left == ALWAYS || right == ALWAYS) return ALWAYS;
        return left.or(right);
    }

    /**
     * Adds the given value to a tag's membership, widening it if the value is already present.
     *
     * @param membership the membership being built
     * @param value      the value to add
     * @param predicate  the context in which the value belongs to the tag
     */
    public static <T> void add(Map<T, Predicate<Context>> membership, T value, Predicate<Context> predicate) {
        if (predicate == ALWAYS) membership.put(value, ALWAYS);
        else membership.merge(value, predicate, TagConditions::or);
    }

    /**
     * Removes the given value from a tag's membership, narrowing it if the removal is conditional.
     *
     * <p>A value that isn't a member is left alone, so removing something that a data pack never
     * added is not an error.
     *
     * @param membership the membership being built
     * @param value      the value to remove
     * @param predicate  the context in which the value stops belonging to the tag
     */
    public static <T> void remove(Map<T, Predicate<Context>> membership, T value, Predicate<Context> predicate) {
        if (predicate == ALWAYS) membership.remove(value);
        else membership.computeIfPresent(value, (key, existing) -> and(existing, predicate.negate()));
    }
}
