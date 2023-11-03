package net.modificationstation.stationapi.mixin.dimension;

import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;
import net.modificationstation.stationapi.api.registry.DimensionRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(World.class)
public class MixinLevel {

    @Shadow protected WorldProperties properties;

    @Redirect(
            method = "<init>(Lnet/minecraft/level/dimension/DimensionData;Ljava/lang/String;JLnet/minecraft/level/dimension/Dimension;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/level/LevelProperties;getDimensionId()I"
            )
    )
    private int modIf(WorldProperties levelProperties) {
        return DimensionRegistry.INSTANCE.getByLegacyId(levelProperties.getDimensionId()).map(dimensionSupplier -> -1).orElse(0);
    }

    @ModifyConstant(
            method = "<init>(Lnet/minecraft/level/dimension/DimensionData;Ljava/lang/String;JLnet/minecraft/level/dimension/Dimension;)V",
            constant = @Constant(
                    intValue = -1,
                    ordinal = 1
            )
    )
    private int getDimensionId(int constant) {
        return properties.getDimensionId();
    }
}
