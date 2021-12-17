package net.modificationstation.stationapi.api.registry;

import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMaps;
import it.unimi.dsi.fastutil.ints.IntComparator;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static net.modificationstation.stationapi.api.StationAPI.MODID;
import static net.modificationstation.stationapi.api.registry.Identifier.of;

public final class DimensionRegistry extends LevelSerialRegistry<DimensionContainer<?>> {

    public static final DimensionRegistry INSTANCE = new DimensionRegistry();

    private static final int
            VANILLA_MIN = -1,
            VANILLA_MAX = 0;

    /**
     * Custom comparator which preserves vanilla-like dimension ID order.
     * It puts vanilla-range [-1; 0] dimensions first
     * and sorts them by opposite order, followed by non-vanilla dimensions in natural order.
     *
     * <p>It doesn't include skylands in the vanilla range,
     * because skylands were never properly implemented in the game,
     * so we don't know where they're supposed to be in the range,
     * whether it's the first position, so it follows the opposite order,
     * second position so overworld is first as the main world,
     * which is then followed by opposite order, or overworld first, followed by natural order instead.
     */
    public static final IntComparator DIMENSIONS_COMPARATOR = (a, b) -> {
        if (a == b)
            return 0;
        boolean
                aVanilla = VANILLA_MIN <= a && a <= VANILLA_MAX,
                bVanilla = VANILLA_MIN <= b && b <= VANILLA_MAX;
        if (aVanilla && bVanilla)
            return a < b ? 1 : -1;
        if (aVanilla)
            return -1;
        if (bVanilla)
            return 1;
        return a < b ? -1 : 1;
    };

    /**
     * {@link DimensionRegistry#serialView} backend.
     *
     * <p>Uses an AVL tree map for faster read operations,
     * since dimensions are registered only once,
     * so the map doesn't require fast write operations.
     */
    private final Int2ObjectSortedMap<DimensionContainer<?>> values = new Int2ObjectAVLTreeMap<>(DIMENSIONS_COMPARATOR);

    /**
     * Since Minecraft itself doesn't hold any kind of dynamic dimension lookup,
     * we have to add our own in the {@link DimensionRegistry}.
     *
     * <p>This is a sorted int->{@link DimensionContainer} map.
     * It uses a custom comparator, see {@link DimensionRegistry#DIMENSIONS_COMPARATOR}.
     *
     * <p>This map is unmodifiable, but it updates itself to always reflect the internal serial map, {@link DimensionRegistry#values}.
     */
    public final Int2ObjectSortedMap<DimensionContainer<?>> serialView = Int2ObjectSortedMaps.unmodifiable(values);

    private boolean registering;

    private DimensionRegistry() {
        super(of(MODID, "dimensions"));
    }

    @Override
    public int getSize() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getSerialID(@NotNull DimensionContainer<?> value) {
        return value.serialID;
    }

    @Override
    public @NotNull Optional<DimensionContainer<?>> get(int serialID) {
        return Optional.ofNullable(serialView.get(serialID));
    }

    @Override
    public int getSerialIDShift() {
        return 0;
    }

    @Override
    protected void remap(int newSerialID, @NotNull DimensionContainer<?> value) {
        Identifier id = getIdentifier(value);
        unregister(id);
        values.remove(getSerialID(value));
        if (serialView.containsKey(newSerialID))
            remap(getNextSerialID(), serialView.get(newSerialID));
        value.serialID = newSerialID;
        super.register(id, value);
        values.put(newSerialID, value);
    }

    @Override
    public void register(@NotNull Identifier identifier, @NotNull DimensionContainer<?> value) {
        if (registering)
            super.register(identifier, value);
        else {
            registering = true;
            register(identifier, id -> {
                value.serialID = id;
                values.put(id, value);
                return value;
            });
            registering = false;
        }
    }

    public void register(@NotNull Identifier identifier, int serialID, @NotNull DimensionContainer<?> value) {
        value.serialID = serialID;
        values.put(serialID, value);
        super.register(identifier, value);
    }
}
