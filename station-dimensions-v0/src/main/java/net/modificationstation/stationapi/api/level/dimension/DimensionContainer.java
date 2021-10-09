package net.modificationstation.stationapi.api.level.dimension;

import net.minecraft.level.dimension.Dimension;
import org.jetbrains.annotations.NotNull;

import java.util.function.*;

public class DimensionContainer<T extends Dimension> {

    @NotNull
    public final Supplier<@NotNull T> factory;
    public int serialID;

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
        return obj instanceof DimensionContainer && serialID == ((@NotNull DimensionContainer<?>) obj).serialID;
    }

    @Override
    public int hashCode() {
        return serialID;
    }
}
