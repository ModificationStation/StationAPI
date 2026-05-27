package net.modificationstation.stationapi.api.util.context;

import org.jetbrains.annotations.Nullable;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

/**
 * A context that includes an actor that performed the action.
 */
public interface ActorContext extends Context {
    /**
     * The context key used to evaluate actor-related conditions.
     */
    Context.Key<Object> ACTOR = new Context.Key<>(NAMESPACE.id("actor"));

    static ActorContext of(Context context) {
        return context instanceof ActorContext a ? a : context::getRaw;
    }

    /**
     * {@return the actor performing the action, or {@code null} if none}
     */
    default @Nullable Object actor() {
        return get(ACTOR);
    }
}
