package net.modificationstation.stationapi.mixin.dimension;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;
import net.modificationstation.stationapi.api.dimension.v1.registry.DimensionTypeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(World.class)
class WorldMixin {
    @Shadow protected WorldProperties properties;

    @WrapOperation(
            method = "<init>(Lnet/minecraft/world/storage/WorldStorage;Ljava/lang/String;JLnet/minecraft/world/dimension/Dimension;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/WorldProperties;getDimensionId()I"
            )
    )
    private int stationapi_modIf(WorldProperties worldProperties, Operation<Integer> original) {
        return DimensionTypeRegistry.INSTANCE.getEntryByLogicalId(worldProperties.getDimensionId()).map(dimensionSupplier -> -1).orElse(0);
    }

    @ModifyExpressionValue(
            method = "<init>(Lnet/minecraft/world/storage/WorldStorage;Ljava/lang/String;JLnet/minecraft/world/dimension/Dimension;)V",
            at = @At(
                    value = "CONSTANT",
                    args = "intValue=-1",
                    ordinal = 1
            )
    )
    private int stationapi_getDimensionId(int constant) {
        return properties.getDimensionId();
    }
}
