package net.modificationstation.stationapi.api.registry;

import net.minecraft.level.dimension.Dimension;
import net.modificationstation.stationapi.api.registry.serial.SerialIDHolder;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;

public class DimensionContainer<T extends Dimension> implements SerialIDHolder {

    @NotNull
    public final Supplier<@NotNull T> factory;
    int serialID;

    public DimensionContainer(@NotNull IntFunction<@NotNull T> factory) {
        this((@NotNull Function<@NotNull DimensionContainer<@NotNull T>, @NotNull Supplier<@NotNull T>>) dimensionContainer -> () -> factory.apply(dimensionContainer.serialID));
    }

    public DimensionContainer(@NotNull Supplier<@NotNull T> factory) {
        this((@NotNull Function<@NotNull DimensionContainer<@NotNull T>, @NotNull Supplier<@NotNull T>>) dimensionContainer -> factory);
    }

    private DimensionContainer(@NotNull Function<@NotNull DimensionContainer<@NotNull T>, @NotNull Supplier<@NotNull T>> factoryFactory) {
        this.factory = factoryFactory.apply(this);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof @NotNull DimensionContainer<?> dimensionContainer && serialID == dimensionContainer.serialID;
    }

    @Override
    public int hashCode() {
        return serialID;
    }

    @Override
    public int getSerialID() {
        return serialID;
    }
}
