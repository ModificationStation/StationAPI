package net.modificationstation.stationapi.mixin.dimension.server;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.modificationstation.stationapi.api.dimension.v1.registry.DimensionTypeRegistry;
import net.modificationstation.stationapi.api.world.dimension.VanillaDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayNetworkHandler.class)
class ServerPlayNetworkHandlerMixin {
    @Shadow private ServerPlayerEntity player;

    @ModifyExpressionValue(
            method = "onPlayerRespawn",
            at = @At(
                    value = "CONSTANT",
                    args = "intValue=0"
            )
    )
    private int stationapi_modifyRespawnDimension(int original) {
        final var registry = DimensionTypeRegistry.INSTANCE;
        return player.world.dimension.hasWorldSpawn()
                ? player.dimensionId
                : registry.getLogicalId(registry.get(VanillaDimensions.OVERWORLD));
    }
}
