package net.modificationstation.stationapi.api.registry;

import net.modificationstation.stationapi.api.level.dimension.DimensionContainer;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static net.modificationstation.stationapi.api.StationAPI.MODID;
import static net.modificationstation.stationapi.api.registry.Identifier.of;

public final class DimensionRegistry extends LevelSerialRegistry<DimensionContainer<?>> {

    public static final DimensionRegistry INSTANCE = new DimensionRegistry();

    private final TreeMap<Integer, DimensionContainer<?>> values = new TreeMap<>();
    public final NavigableMap<Integer, DimensionContainer<?>> serialView = Collections.unmodifiableNavigableMap(values);

    private boolean badcode;

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
        if (badcode)
            super.register(identifier, value);
        else {
            badcode = true;
            register(identifier, id -> {
                value.serialID = id;
                values.put(id, value);
                return value;
            });
            badcode = false;
        }
    }

    public void register(@NotNull Identifier identifier, int serialID, @NotNull DimensionContainer<?> value) {
        value.serialID = serialID;
        values.put(serialID, value);
        super.register(identifier, value);
    }
}
