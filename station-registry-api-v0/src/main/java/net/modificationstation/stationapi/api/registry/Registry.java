package net.modificationstation.stationapi.api.registry;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.*;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

/**
 * Registry class is used to map an {@link Identifier} to its object to keep track of the objects by identifiers.
 *
 * <p>For example, "minecraft:dirt" -> {@link net.minecraft.block.BlockBase#DIRT}
 *
 * <p>The registry's contract is that every object must have an identifier, but not every identifier must have an object.
 *
 * <p>Each registry has its own identifier in the {@link Registry#id} field.
 *
 * <p>Registry implements {@link Iterable}, so it can be iterated through with a for-each loop.
 * Alongside iteration, there's {@link Registry#computeIfAbsent(Identifier, Function)} method,
 * which will check if the identifier has an object mapped to it, and if it doesn't,
 * the provided function will be invoked and the returned object will be mapped to the given identifier.
 *
 * @param <T> the object's type that's stored in the registry.
 * @author mine_diver
 * @see AbstractSerialRegistry
 * @see LevelSerialRegistry
 */
public class Registry<T> implements Iterable<Map.Entry<Identifier, T>> {

    /**
     * Registry of registries.
     *
     * <p>Contains all registries mapped to their identifiers and contains itself recursively.
     *
     * <p>Used to access/iterate through all registries by their identifiers.
     */
    @NotNull
    public static final Registry<Registry<?>> REGISTRIES = new Registry<>(Identifier.of(MODID, "registries"), true);

    /**
     * Registry's identifier.
     *
     * <p>For example, "minecraft:blocks".
     */
    @NotNull
    public final Identifier id;

    /**
     * {@link BiMap} with identifiers mapped to registry's objects.
     *
     * <p>A bimap provides just the right functionality for registries.
     * Both keys and values (identifiers and registry's objects) have to be unique,
     * meaning none of the identifiers can be mapped to multiple objects
     * and none of the objects can be mapped to multiple identifiers.
     * It also allows for fast and easy inverse lookup without having a second {@link Map} with inverse values.
     */
    @NotNull
    private final BiMap<Identifier, T> values = HashBiMap.create();

    /**
     * Default registry constructor.
     * @param identifier registry's identifier.
     */
    public Registry(@NotNull Identifier identifier) {
        this(identifier, false);
    }

    /**
     * Internal registry constructor used for the registry of registries to avoid {@link NullPointerException}
     * when referencing {@link Registry#REGISTRIES} before it getting its object.
     * @param identifier registry's identifier.
     * @param isRegistryRegistry whether or not the current registry is the registry of registries.
     *                           If it is, it registers itself in itself, otherwise registries itself in {@link Registry#REGISTRIES}.
     */
    private Registry(@NotNull Identifier identifier, boolean isRegistryRegistry) {
        this.id = Objects.requireNonNull(identifier);
        if (isRegistryRegistry)
            //noinspection unchecked
            register(id, (T) this);
        else
            REGISTRIES.register(id, this);
    }

    /**
     * The basic register method that adds the given object mapped to the given identifier to the registry.
     * @param identifier the identifier to assign to the object.
     * @param value the object to assign to the identifier.
     */
    public void register(@NotNull Identifier identifier, @NotNull T value) {
        values.put(Objects.requireNonNull(identifier), Objects.requireNonNull(value));
    }

    /**
     * Returns the object associated to this identifier.
     * <p>Note, since not all identifiers are supposed to have an object assigned to them,
     * an {@link Optional} containing the object is returned instead.
     * If the given identifier doesn't have an object assigned to it, the optional will be empty.
     * @param identifier the identifier of the requested object.
     * @return an {@link Optional} containing the object associated to the given identifier,
     * or an empty optional if there's no object associated to this identifier.
     */
    public @NotNull Optional<T> get(@NotNull Identifier identifier) {
        return Optional.ofNullable(values.get(Objects.requireNonNull(identifier)));
    }

    /**
     * Returns the identifier associated to this object.
     * <p>Since every object is supposed to have an identifier, an {@link Optional} isn't required here.
     * @param value the object associated to the requested identifier.
     * @return the identifier of the given object.
     */
    public @NotNull Identifier getIdentifier(@NotNull T value) {
        return values.inverse().get(Objects.requireNonNull(value));
    }

    /**
     * Returns an iterator over a set of {@link Map.Entry} containing identifiers as keys and objects assigned to them as values.
     * @return an iterator over a set of {@link Map.Entry} containing identifiers as keys and objects assigned to them as values.
     */
    @Override
    public @NotNull Iterator<Map.Entry<Identifier, T>> iterator() {
        return values.entrySet().iterator();
    }

    /**
     * Simplified version of {@link Registry#forEach(Consumer)} with action being a {@link BiConsumer} of
     * {@link Identifier} and registry's object instead of a {@link Consumer} of {@link Map.Entry} of those.
     * @param action the action to be performed for each element.
     */
    public void forEach(@NotNull BiConsumer<Identifier, T> action) {
        Objects.requireNonNull(action);
        for (Map.Entry<Identifier, T> identifierTEntry : this) {
            Identifier k;
            T v;
            try {
                k = identifierTEntry.getKey();
                v = identifierTEntry.getValue();
            } catch (IllegalStateException ise) {
                // this usually means the entry is no longer in the map.
                throw new ConcurrentModificationException(ise);
            }
            action.accept(k, v);
        }
    }

    /**
     * If the specified identifier is not already associated with an object,
     * attempts to compute its object using the given mapping function and adds it into this registry.
     * @param identifier identifier with which the specified object is to be associated.
     * @param function the function to compute an object.
     * @return the current (existing or computed) object associated with the specified identifier.
     */
    public @NotNull T computeIfAbsent(@NotNull Identifier identifier, @NotNull Function<@NotNull Identifier, @NotNull T> function) {
        Objects.requireNonNull(identifier);
        Objects.requireNonNull(function);
        return get(identifier).orElseGet(() -> {
            T value = function.apply(identifier);
            register(identifier, value);
            return value;
        });
    }
}
