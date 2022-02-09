package net.modificationstation.stationapi.api.registry;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Keyable;
import com.mojang.serialization.Lifecycle;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenCustomHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.modificationstation.stationapi.api.util.Util;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

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
public class Registry<T> implements Iterable<Map.Entry<Identifier, T>>, Codec<T>, Keyable {

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
    protected final BiMap<Identifier, T> values = HashBiMap.create();

    private final Lifecycle lifecycle;

    private final ObjectList<T> rawIdToEntry = new ObjectArrayList<>(256);
    private final Object2IntMap<T> entryToRawId = new Object2IntOpenCustomHashMap<>(Util.identityHashStrategy());
    private int nextId;
    private final Map<T, Lifecycle> entryToLifecycle = new IdentityHashMap<>();

    /**
     * Default registry constructor.
     * @param identifier registry's identifier.
     */
    public Registry(final @NotNull Identifier identifier) {
        this(identifier, false);
    }

    /**
     * Internal registry constructor used for the registry of registries to avoid {@link NullPointerException}
     * when referencing {@link Registry#REGISTRIES} before it getting its object.
     * @param identifier registry's identifier.
     * @param isRegistryRegistry whether or not the current registry is the registry of registries.
     *                           If it is, it registers itself in itself, otherwise registries itself in {@link Registry#REGISTRIES}.
     */
    private Registry(final @NotNull Identifier identifier, final boolean isRegistryRegistry) {
        this.id = Objects.requireNonNull(identifier);
        if (isRegistryRegistry)
            //noinspection unchecked
            register(id, (T) this);
        else
            REGISTRIES.register(id, this);
        this.lifecycle = Lifecycle.experimental();
    }

    /**
     * The basic register method that adds the given object mapped to the given identifier to the registry.
     * @param identifier the identifier to assign to the object.
     * @param value the object to assign to the identifier.
     */
    public void register(final @NotNull Identifier identifier, final @Nullable T value) {
        register(nextId, identifier, value, Lifecycle.stable());
    }

    private void register(int rawId, final @NotNull Identifier identifier, final @Nullable T value, Lifecycle lifecycle) {
        rawIdToEntry.size(Math.max(this.rawIdToEntry.size(), rawId + 1));
        this.rawIdToEntry.set(rawId, value);
        this.entryToRawId.put(value, rawId);
        values.put(Objects.requireNonNull(identifier), value);
        entryToLifecycle.put(value, lifecycle);
        if (nextId <= rawId) {
            nextId = rawId + 1;
        }
    }

    /**
     * Unregisters the value with the given identifier.
     * @param identifier the identifier of the value that should get unregistered.
     */
    public void unregister(final @NotNull Identifier identifier) {
        values.remove(identifier);
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
    public @NotNull Optional<T> get(final @NotNull Identifier identifier) {
        return Optional.ofNullable(values.get(Objects.requireNonNull(identifier)));
    }

    /**
     * Returns the identifier associated to this object.
     * <p>Since every object is supposed to have an identifier, an {@link Optional} isn't required here.
     * @param value the object associated to the requested identifier.
     * @return the identifier of the given object.
     */
    public @NotNull Identifier getIdentifier(final T value) {
        return values.inverse().get(value);
    }

    @ApiStatus.Internal
    public T getByRawId(int rawId) {
        return rawIdToEntry.get(rawId);
    }

    @ApiStatus.Internal
    public int getRawId(T value) {
        return entryToRawId.getInt(value);
    }

    protected Lifecycle getEntryLifecycle(T value) {
        return entryToLifecycle.get(value);
    }

    /**
     * Returns an iterator over a set of {@link Map.Entry} containing identifiers as keys and objects assigned to them as values.
     * @return an iterator over a set of {@link Map.Entry} containing identifiers as keys and objects assigned to them as values.
     */
    @Override
    public @NotNull Iterator<Map.Entry<Identifier, T>> iterator() {
        return new HashSet<>(values.entrySet()).iterator();
    }

    /**
     * Simplified version of {@link Registry#forEach(Consumer)} with action being a {@link BiConsumer} of
     * {@link Identifier} and registry's object instead of a {@link Consumer} of {@link Map.Entry} of those.
     * @param action the action to be performed for each element.
     */
    public void forEach(final @NotNull BiConsumer<Identifier, T> action) {
        Objects.requireNonNull(action);
        for (Map.Entry<Identifier, T> identifierTEntry : this) {
            final Identifier k;
            final T v;
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
    public @NotNull T computeIfAbsent(final @NotNull Identifier identifier, final @NotNull Function<@NotNull Identifier, @NotNull T> function) {
        Objects.requireNonNull(identifier);
        Objects.requireNonNull(function);
        return get(identifier).orElseGet(() -> {
            final T value = function.apply(identifier);
            register(identifier, value);
            return value;
        });
    }

    public Stream<Map.Entry<Identifier, T>> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    public Stream<Map.Entry<Identifier, T>> parallelStream() {
        return StreamSupport.stream(spliterator(), true);
    }

    @Override
    public <U> DataResult<Pair<T, U>> decode(DynamicOps<U> dynamicOps, U object) {
        return dynamicOps.compressMaps() ? dynamicOps.getNumberValue(object).flatMap((number) -> {
            T value = this.getByRawId(number.intValue());
            return value == null ? DataResult.error("Unknown registry id: " + number) : DataResult.success(value, this.getEntryLifecycle(value));
        }).map((objectx) -> Pair.of(objectx, dynamicOps.empty())) : Identifier.CODEC.decode(dynamicOps, object).flatMap((pair) -> {
            Optional<T> valueOptional = this.get(pair.getFirst());
            return valueOptional.map(t -> DataResult.success(Pair.of(t, pair.getSecond()), this.getEntryLifecycle(t))).orElseGet(() -> DataResult.error("Unknown registry key: " + pair.getFirst()));
        });
    }

    @Override
    public <U> DataResult<U> encode(T object, DynamicOps<U> dynamicOps, U object2) {
        Identifier identifier = this.getIdentifier(object);
        //noinspection ConstantConditions
        if (identifier == null) {
            return DataResult.error("Unknown registry element " + object);
        } else {
            return dynamicOps.compressMaps() ? dynamicOps.mergeToPrimitive(object2, dynamicOps.createInt(this.getRawId(object))).setLifecycle(this.lifecycle) : dynamicOps.mergeToPrimitive(object2, dynamicOps.createString(identifier.toString())).setLifecycle(this.lifecycle);
        }
    }

    @Override
    public <U> Stream<U> keys(DynamicOps<U> dynamicOps) {
        return values.keySet().stream().map((identifier) -> dynamicOps.createString(identifier.toString()));
    }
}
