package net.modificationstation.stationapi.api.registry;

import com.mojang.serialization.Lifecycle;
import it.unimi.dsi.fastutil.ints.*;
import net.modificationstation.stationapi.api.registry.legacy.AbstractInt2ObjectMapBackedLegacyRegistry;
import net.modificationstation.stationapi.api.registry.legacy.LevelLegacyRegistry;
import org.jetbrains.annotations.NotNull;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

public final class DimensionRegistry extends AbstractInt2ObjectMapBackedLegacyRegistry<DimensionContainer<?>> implements LevelLegacyRegistry<DimensionContainer<?>> {

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

    public static final RegistryKey<Registry<DimensionContainer<?>>> KEY = RegistryKey.ofRegistry(MODID.id("dimensions"));
    public static final DimensionRegistry INSTANCE = Registry.create(KEY, new DimensionRegistry(), Lifecycle.experimental());

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
        super(KEY, null);
    }

    @Override
    protected Int2ObjectMap<DimensionContainer<?>> getBackingInt2ObjectMap() {
        return serialView;
    }

    @Override
    public void remap(int newLegacyId, @NotNull DimensionContainer<?> value) {
//        Identifier id = getId(value);
//        unregister(id);
        values.remove(getLegacyId(value));
        if (serialView.containsKey(newLegacyId))
            remap(getNextLegacyId(), serialView.get(newLegacyId));
        value.serialID = newLegacyId;
//        super.register(id, value);
        values.put(newLegacyId, value);
    }

    public void register(@NotNull Identifier identifier, @NotNull DimensionContainer<?> value) {
        if (registering)
            Registry.register(this, identifier, value);
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
        Registry.register(this, identifier, value);
    }
}
