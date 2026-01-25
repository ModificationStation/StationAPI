package net.modificationstation.stationapi.api.dimension.v1;

import net.minecraft.world.dimension.Dimension;
import net.modificationstation.stationapi.api.dimension.v1.registry.DimensionTypeRegistry;
import net.modificationstation.stationapi.api.registry.RegistryEntry;

import java.util.function.Supplier;

public final class DimensionType<DIMENSION extends Dimension> {
    private final RegistryEntry.Reference<DimensionType<DIMENSION>> registryEntry;
    private final Supplier<DIMENSION> factory;
    private final Supplier<Dimension> upcastedFactory;

    private DimensionType(Builder<DIMENSION> builder) {
        //noinspection unchecked
        registryEntry = (RegistryEntry.Reference<DimensionType<DIMENSION>>) (RegistryEntry.Reference<?>)
                DimensionTypeRegistry.INSTANCE.createEntry(this);
        factory = builder.factory;
        upcastedFactory = factory::get;
    }

    public RegistryEntry.Reference<DimensionType<DIMENSION>> registryEntry() {
        return registryEntry;
    }

    public Supplier<DIMENSION> factory() {
        return factory;
    }

    public Supplier<Dimension> upcastedFactory() {
        return upcastedFactory;
    }

    public static <DIMENSION extends Dimension> Builder<DIMENSION> builder(
            Supplier<DIMENSION> factory
    ) {
        return new Builder<>(factory);
    }

    public static final class Builder<DIMENSION extends Dimension> {
        private final Supplier<DIMENSION> factory;

        private Builder(Supplier<DIMENSION> factory) {
            this.factory = factory;
        }

        public DimensionType<DIMENSION> build() {
            return new DimensionType<>(this);
        }
    }
}
