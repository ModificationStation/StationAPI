package net.modificationstation.stationapi.api.dimension.v1;

import net.minecraft.world.dimension.Dimension;
import net.modificationstation.stationapi.api.dimension.v1.registry.DimensionTypeRegistry;
import net.modificationstation.stationapi.api.registry.RegistryEntry;
import net.modificationstation.stationapi.impl.world.dimension.StationDimensionImpl;

public final class DimensionType<DIMENSION extends Dimension> {
    private final RegistryEntry.Reference<DimensionType<DIMENSION>> registryEntry;
    private final DimensionFactory<DIMENSION> factory;

    private DimensionType(Builder<DIMENSION> builder) {
        //noinspection unchecked
        registryEntry = (RegistryEntry.Reference<DimensionType<DIMENSION>>) (RegistryEntry.Reference<?>)
                DimensionTypeRegistry.INSTANCE.createEntry(this);
        factory = builder.factory;
    }

    public RegistryEntry.Reference<DimensionType<DIMENSION>> registryEntry() {
        return registryEntry;
    }

    public DIMENSION create() {
        final var dimension = factory.create(this);
        ((StationDimensionImpl) dimension).stationapi_postInit(this);
        return dimension;
    }

    public Dimension createUpcasted() {
        return create();
    }

    public static <DIMENSION extends Dimension> Builder<DIMENSION> builder(
            DimensionFactory<DIMENSION> factory
    ) {
        return new Builder<>(factory);
    }

    public static final class Builder<DIMENSION extends Dimension> {
        private final DimensionFactory<DIMENSION> factory;

        private Builder(DimensionFactory<DIMENSION> factory) {
            this.factory = factory;
        }

        public DimensionType<DIMENSION> build() {
            return new DimensionType<>(this);
        }
    }
}
