package net.modificationstation.stationapi.mixin.dimension;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.dimension.Dimension;
import net.modificationstation.stationapi.api.dimension.v1.DimensionType;
import net.modificationstation.stationapi.api.dimension.v1.registry.DimensionTypeRegistry;
import net.modificationstation.stationapi.api.registry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Supplier;

@Mixin(Dimension.class)
class DimensionMixin {
    @Shadow public int id;

    @WrapMethod(method = "fromId")
    private static Dimension stationapi_getDimension(int id, Operation<Dimension> original) {
        return DimensionTypeRegistry.INSTANCE.getEntryByLogicalId(id)
                .map(RegistryEntry::value)
                .map(DimensionType::upcastedFactory)
                .map(Supplier::get)
                .orElseGet(() -> original.call(id));
    }
}
