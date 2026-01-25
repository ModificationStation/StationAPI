package net.modificationstation.stationapi.mixin.dimension;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.dimension.Dimension;
import net.modificationstation.stationapi.api.dimension.v1.DimensionType;
import net.modificationstation.stationapi.api.dimension.v1.StationDimension;
import net.modificationstation.stationapi.api.dimension.v1.registry.DimensionTypeRegistry;
import net.modificationstation.stationapi.api.registry.RegistryEntry;
import net.modificationstation.stationapi.impl.world.dimension.StationDimensionImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Dimension.class)
class DimensionMixin implements StationDimensionImpl, StationDimension {
    @Shadow public int id;

    @Unique
    private DimensionType<?> stationapi_type;

    @WrapMethod(method = "fromId")
    private static Dimension stationapi_getDimension(int id, Operation<Dimension> original) {
        return DimensionTypeRegistry.INSTANCE.getEntryByLogicalId(id)
                .map(RegistryEntry::value)
                .map(DimensionType::createUpcasted)
                .orElseGet(() -> original.call(id));
    }

    @Override
    @Unique
    public DimensionType<?> type() {
        return stationapi_type;
    }

    @Override
    @Unique
    public void stationapi_postInit(DimensionType<?> type) {
        stationapi_type = type;
        id = DimensionTypeRegistry.INSTANCE.getLogicalId(type);
    }
}
