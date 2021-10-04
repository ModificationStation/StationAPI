package net.modificationstation.stationapi.api.registry;

import net.minecraft.level.dimension.Dimension;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.*;

import static net.modificationstation.stationapi.api.StationAPI.MODID;
import static net.modificationstation.stationapi.api.registry.Identifier.of;

public final class DimensionRegistry extends LevelSerialRegistry<Supplier<Dimension>> {

    public static final DimensionRegistry INSTANCE = new DimensionRegistry();

    private final TreeMap<Integer, Supplier<Dimension>> values = new TreeMap<>();
    public final SortedMap<Integer, Supplier<Dimension>> serialView = Collections.unmodifiableSortedMap(values);

    private DimensionRegistry() {
        super(of(MODID, "dimensions"));
    }

    @Override
    public int getSize() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getSerialID(@NotNull Supplier<Dimension> value) {
        return values.entrySet().stream().filter(integerSupplierEntry -> value.equals(integerSupplierEntry.getValue())).findFirst().map(Map.Entry::getKey).orElseThrow(() -> new RuntimeException("Couldn't find serial ID for dimension " + value.get().getClass().getName() + "!"));
    }

    @Override
    public @NotNull Optional<Supplier<Dimension>> get(int serialID) {
        return Optional.ofNullable(values.get(serialID));
    }

    @Override
    public int getSerialIDShift() {
        return 0;
    }

    @Override
    protected void remap(int newSerialID, @NotNull Supplier<Dimension> value) {
        values.remove(getSerialID(value));
        if (values.containsKey(newSerialID))
            remap(getNextSerialID(), values.get(newSerialID));
        values.put(newSerialID, value);
    }

    @Override
    public void register(@NotNull Identifier identifier, @NotNull Supplier<Dimension> value) {
        register(identifier, getNextSerialID(), value);
    }

    public void register(@NotNull Identifier identifier, int serialID, @NotNull Supplier<Dimension> value) {
        super.register(identifier, value);
        values.put(serialID, value);
    }
}
