package net.modificationstation.stationapi.mixin.dimension.server;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.ChunkMap;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.modificationstation.stationapi.api.dimension.v1.registry.DimensionTypeRegistry;
import net.modificationstation.stationapi.api.registry.RegistryEntry;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
class ServerPlayerConnectionManagerMixin {
    @Shadow private ChunkMap[] chunkMaps;

    @ModifyExpressionValue(
            method = "<init>(Lnet/minecraft/server/MinecraftServer;)V",
            at = @At(
                    value = "CONSTANT",
                    args = "intValue=2"
            )
    )
    private int stationapi_modifyPlayerViewsSize(int original) {
        return DimensionTypeRegistry.INSTANCE.size();
    }

    @Inject(
            method = "<init>(Lnet/minecraft/server/MinecraftServer;)V",
            at = @At("RETURN")
    )
    private void stationapi_initializePlayerViews(
            MinecraftServer server, CallbackInfo ci,
            @Local(index = 2) int viewRadius
    ) {
        final var registry = DimensionTypeRegistry.INSTANCE;

        if (registry.size() < 3)
            return;

        int[] dimensions = registry.stream().mapToInt(registry::getLogicalId).toArray();
        for (int i = 2; i < dimensions.length; i++)
            chunkMaps[i] = new ChunkMap(server, dimensions[i], viewRadius);
    }

    @Inject(
            method = "updatePlayerAfterDimensionChange(Lnet/minecraft/entity/player/ServerPlayerEntity;)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/entity/player/ServerPlayerEntity;dimensionId:I",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 0
            )
    )
    private void stationapi_addPlayerToDimension(ServerPlayerEntity player, CallbackInfo ci) {
        for (int i = 2; i < chunkMaps.length; i++)
            chunkMaps[i].removePlayer(player);
    }

    @WrapMethod(method = "getChunkMap")
    private ChunkMap stationapi_getChunkMap(int dimension, Operation<ChunkMap> original) {
        final var registry = DimensionTypeRegistry.INSTANCE;
        return registry.getEntryByLogicalId(dimension)
                .map(RegistryEntry::value)
                .map(registry::getRawId)
                .map(id -> chunkMaps[id])
                .orElseGet(() -> original.call(dimension));
    }
}
