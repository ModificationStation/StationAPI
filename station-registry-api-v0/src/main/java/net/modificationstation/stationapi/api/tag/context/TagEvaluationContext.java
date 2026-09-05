package net.modificationstation.stationapi.api.tag.context;

import net.modificationstation.stationapi.api.util.context.Context;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

@FunctionalInterface
public interface TagEvaluationContext extends Context {
    Context.Key<Boolean> IGNORE_TAG_CONDITIONS_KEY = new Context.Key<>(NAMESPACE.id("ignore_tag_conditions"));
    
    TagEvaluationContext DEFAULT = of(Context.EMPTY);
    TagEvaluationContext BYPASSED = of(Context.EMPTY.with(IGNORE_TAG_CONDITIONS_KEY, true));

    default boolean ignoreTagConditions() {
        return Boolean.TRUE.equals(get(IGNORE_TAG_CONDITIONS_KEY));
    }
    
    static TagEvaluationContext of(boolean ignoreTagConditions) {
        return ignoreTagConditions ? BYPASSED : DEFAULT;
    }

    static TagEvaluationContext of(Context context) {
        if (context instanceof TagEvaluationContext t) return t;
        interface TagEvaluationContextDelegate extends TagEvaluationContext, Delegate {}
        return (TagEvaluationContextDelegate) () -> context;
    }
}
