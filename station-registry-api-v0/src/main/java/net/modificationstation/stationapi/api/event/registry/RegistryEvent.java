package net.modificationstation.stationapi.api.event.registry;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.mine_diver.unsafeevents.Event;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.function.BulkBiConsumer;
import org.jetbrains.annotations.Contract;

import java.util.function.ToIntFunction;

/**
 * A superclass for events involving registries.
 *
 * @param <REGISTRY> the type of the registry involved in this event.
 * @author mine_diver
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class RegistryEvent<REGISTRY extends Registry<?>> extends Event {
    /**
     * The instance of the event's registry.
     */
    public final REGISTRY registry;

    public static abstract class EntryTypeBound<ENTRY, REGISTRY extends Registry<ENTRY>> extends RegistryEvent<REGISTRY> {
        protected EntryTypeBound(REGISTRY registry) {
            super(registry);
        }

        public ENTRY register(Identifier id, ENTRY entry) {
            return Registry.register(registry, id, entry);
        }

        public ENTRY register(int rawId, Identifier id, ENTRY entry) {
            return Registry.register(registry, rawId, id, entry);
        }

        @Contract(pure = true)
        public BulkBiConsumer<Identifier, ENTRY> register() {
            return Registry.register(registry);
        }

        @Contract(pure = true)
        public BulkBiConsumer<String, ENTRY> register(Namespace namespace) {
            return Registry.register(registry, namespace);
        }

        @Contract(pure = true)
        public BulkBiConsumer<String, ENTRY> register(ToIntFunction<ENTRY> rawIdGetter, Namespace namespace) {
            return Registry.register(registry, rawIdGetter, namespace);
        }
    }
}
