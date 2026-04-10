package net.modificationstation.stationapi.api.event.registry;

import com.mojang.serialization.Lifecycle;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.mine_diver.unsafeevents.Event;
import net.modificationstation.stationapi.api.registry.MutableLogicalRegistry;
import net.modificationstation.stationapi.api.registry.MutableRegistry;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.registry.RegistryKey;
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

    public static abstract class EntryTypeBound<ENTRY, REGISTRY extends MutableRegistry<ENTRY>> extends RegistryEvent<REGISTRY> {
        protected EntryTypeBound(REGISTRY registry) {
            super(registry);
        }

        public ENTRY register(Identifier id, ENTRY entry) {
            return registry.add(RegistryKey.of(registry.getKey(), id), entry, Lifecycle.stable()).value();
        }

        public ENTRY register(int rawId, Identifier id, ENTRY entry) {
            return registry.set(rawId, RegistryKey.of(registry.getKey(), id), entry, Lifecycle.stable()).value();
        }

        @Contract(pure = true)
        public BulkBiConsumer<Identifier, ENTRY> register() {
            return BulkBiConsumer.of(this::register);
        }

        @Contract(pure = true)
        public BulkBiConsumer<String, ENTRY> register(Namespace namespace) {
            return BulkBiConsumer.of((id, entry) -> register(namespace.id(id), entry));
        }

        @Contract(pure = true)
        public BulkBiConsumer<String, ENTRY> register(ToIntFunction<ENTRY> rawIdGetter, Namespace namespace) {
            return BulkBiConsumer.of((id, entry) -> register(rawIdGetter.applyAsInt(entry), namespace.id(id), entry));
        }
    }

    public static abstract class Logical<ENTRY, REGISTRY extends MutableLogicalRegistry<ENTRY> & MutableRegistry<ENTRY>> extends EntryTypeBound<ENTRY, REGISTRY> {
        protected Logical(REGISTRY registry) {
            super(registry);
        }

        public ENTRY registerLogical(int logicalId, Identifier id, ENTRY entry) {
            return registry.add(logicalId, RegistryKey.of(registry.getKey(), id), entry, Lifecycle.stable()).value();
        }

        public ENTRY registerLogical(int rawId, int logicalId, Identifier id, ENTRY entry) {
            return registry.set(rawId, logicalId, RegistryKey.of(registry.getKey(), id), entry, Lifecycle.stable()).value();
        }
    }
}
