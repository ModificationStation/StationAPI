package net.modificationstation.stationapi.api.registry.legacy;

import com.mojang.serialization.Lifecycle;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.registry.RegistryKey;
import net.modificationstation.stationapi.api.registry.SimpleRegistry;
import net.modificationstation.stationapi.api.registry.serial.LegacyIDHolder;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.IntFunction;

/**
 * Abstract extension of {@link SimpleRegistry} which allows interaction with objects that also have legacy IDs assigned to them.
 *
 * <p>For example, "minecraft:dirt" -> {@link net.minecraft.block.Block#DIRT}; "minecraft:dirt" -> 3 (legacy ID)
 *
 * <p>Legacy IDs act like identifiers. Every object must have a legacy ID, but not all legacy IDs must have an object.
 * Legacy IDs are integers.
 *
 * <p>Unlike identifiers though, legacy IDs are limited and can be iterated through, as they're just integer numbers.
 *
 * <p>This registry has a lot of abstract methods to allow direct interaction with already existing methods of
 * legacy ID lookup, for example {@link net.minecraft.block.Block#id} and {@link net.minecraft.block.Block#BLOCKS}.
 *
 * @param <T> the object's type that's stored in the registry.
 * @author mine_diver
 * @see Registry
 * @see LevelLegacyRegistry
 */
public abstract class AbstractLegacyRegistry<T> extends SimpleRegistry<T> {

    /**
     * This flag defines whether should the next free legacy ID be shifted to 0->size-shift range
     * during object initialization in {@link AbstractLegacyRegistry#register(Identifier, IntFunction)} method.
     *
     * <p>This is set to true in ItemRegistry due to quirkiness of item constructor and reserved legacy IDs for block items.
     */
    private final boolean shiftLegacyIDOnRegister;

    /**
     * Default registry constructor.
     *
     * @param key                  registry's key.
     * @param intrusive            whether the registry is intrusive
     */
    public AbstractLegacyRegistry(@NotNull RegistryKey<? extends Registry<T>> key, boolean intrusive) {
        this(key, false, intrusive);
    }

    /**
     * Constructor that allows to change the {@link AbstractLegacyRegistry#shiftLegacyIDOnRegister} flag.
     *
     * <p>Used by ItemRegistry.
     *
     * @param key                     registry's key.
     * @param shiftLegacyIDOnRegister whether the next free serial ID should be shifted
     *                                to 0->size-shift range during object initialization.
     * @param intrusive               whether the registry is intrusive
     */
    public AbstractLegacyRegistry(@NotNull RegistryKey<? extends Registry<T>> key, boolean shiftLegacyIDOnRegister, boolean intrusive) {
        super(key, Lifecycle.experimental(), intrusive);
        this.shiftLegacyIDOnRegister = shiftLegacyIDOnRegister;
    }

    /**
     * Defines registry's legacy IDs limit.
     *
     * <p>For example, the length of {@link net.minecraft.block.Block#BLOCKS} array.
     *
     * @return the maximum legacy ID value (exclusive).
     */
    public abstract int getSize();

    /**
     * Returns object's legacy ID.
     *
     * <p>Since every object is supposed to have a legacy ID, {@link OptionalInt} isn't required here.
     *
     * @param value the object associated to the requested legacy ID.
     * @return the legacy ID of the given object.
     */
    public int getLegacyId(@NotNull T value) {
        if (value instanceof LegacyIDHolder holder)
            return holder.getLegacyID();
        else for (int i = 0; i < getSize(); i++) {
            Optional<T> item = getByLegacyId(i);
            if (item.isPresent() && item.get().equals(value))
                return i;
        }
        throw new NoSuchElementException("Couldn't find given object in the registry!");
    }

    /**
     * Returns the legacy ID of object associated to the given identifier.
     *
     * <p>Note, since not every identifier is supposed to have an object associated to it,
     * not every identifier is supposed to have a corresponding legacy ID, so {@link OptionalInt} is required here.
     *
     * @param identifier the identifier of object associated to the requested legacy ID.
     * @return the legacy ID of object associated to the given identifier.
     */
    public @NotNull OptionalInt getLegacyId(@NotNull Identifier identifier) {
        return Optional.ofNullable(get(Objects.requireNonNull(identifier))).map(t -> OptionalInt.of(getLegacyId(t))).orElse(OptionalInt.empty());
    }

    /**
     * Returns the identifier of object associated to the given legacy ID.
     *
     * <p>Note, since not every legacy ID is supposed to have an object associated to it,
     * not every legacy ID is supposed to have a corresponding identifier, so {@link Optional} is required here.
     *
     * @param legacyId the legacy ID of object associated to the requested identifier.
     * @return the identifier of object associated to the given legacy ID.
     */
    public @NotNull Optional<Identifier> getIdByLegacyId(int legacyId) {
        return getByLegacyId(legacyId).map(this::getId);
    }

    /**
     * Returns the object associated to the given serial ID.
     *
     * <p>Note, since not every serial ID is supposed to have an object associated to it,
     * an {@link Optional} is returned instead of the object itself.
     *
     * @param serialID the serial ID of the requested object.
     * @return an {@link Optional} containing the object associated to the given serial ID,
     * or an empty optional if there's no object associated to it.
     */
    public abstract @NotNull Optional<T> getByLegacyId(int serialID);

    /**
     * Defines the first serial ID (inclusive).
     *
     * <p>This is useful if the registry in question has preserved serial IDs for some internal behavior,
     * or if the serial IDs can be negative.
     *
     * <p>For example, a block with serial ID 0 is null because that's how Minecraft handles air blocks,
     * but if we try searching for a free serial ID, it'll be considered free, which will cause
     * a lot of unpredictable behavior and crashes. Thus, shifting the first serial ID to 1 allows us to
     * avoid such scenarios.
     *
     * @return the serial ID shift (inclusive).
     */
    public int getLegacyIdShift() {
        return 0;
    }

    /**
     * Searches for a free serial ID starting from {@link AbstractLegacyRegistry#getLegacyIdShift()} (inclusive)
     * to {@link AbstractLegacyRegistry#getSize()} (exclusive).
     *
     * <p>If a serial ID doesn't have a value associated to it (the returned {@link Optional} is empty),
     * then the serial ID is considered free.
     *
     * @return the found free serial ID.
     * @throws IndexOutOfBoundsException if there are no free serial IDs left in the range.
     */
    public int getNextLegacyId() {
        int i = getLegacyIdShift();
        do while (i < getSize()) {
            if (getByLegacyId(i).isEmpty()) return i;
            i++;
        } while (growSize());
        throw new IndexOutOfBoundsException("No more free serial IDs left for " + getKey() + " registry!");
    }

    protected boolean growSize() {
        int currentSize = getSize();
        return currentSize < Integer.MAX_VALUE && setSize(MathHelper.isPowerOfTwo(currentSize) ? currentSize * 2 : MathHelper.smallestEncompassingPowerOfTwo(currentSize));
    }

    protected boolean setSize(int newSize) {
        return getSize() == newSize;
    }

    /**
     * Returns the next serial ID but shifted to 0->size-shift range by subtracting the {@link AbstractLegacyRegistry#getLegacyIdShift()}.
     *
     * <p>This is useful for the ItemRegistry, in which usual items take IDs from 0->size-shift range
     * (shift being 256, the default size of BlockRegistry),
     * but {@link net.minecraft.item.Item#id} has the true ID that's shifted back to shift->size range.
     *
     * @return the next serial ID but shifted to 0.
     */
    public int getNextLegacyIdShifted() {
        return getNextLegacyId() - getLegacyIdShift();
    }

    /**
     * This register method acts like a shortcut for initializing an object by giving it a free serial ID
     * and adding it to the registry with the given {@code identifier}.
     *
     * <p>A practical use case would be:
     * <p><code>myCoolBlock = registry.register(Identifier.of(NAMESPACE, "my_cool_block"), MyCoolBlock::new).setTranslationKey(MODID, "myCoolBlock");</code>
     *
     * @param identifier the identifier that should be associated to the object.
     * @param initializer the function that initializes the object with the serial ID (for example, {@code MyCoolBlock::new}).
     * @param <E> a subtype of object's type. Useful so that you get for example {@code MyCoolBlock} on the return
     *           instead of {@code BlockBase}.
     * @return the initialized object.
     * @throws IndexOutOfBoundsException if there are no free serial IDs left.
     */
    public <E extends T> @NotNull E register(@NotNull Identifier identifier, IntFunction<@NotNull E> initializer) {
        E value = initializer.apply(shiftLegacyIDOnRegister ? getNextLegacyIdShifted() : getNextLegacyId());
        Registry.register(this, identifier, value);
        return value;
    }
}
