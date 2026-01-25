package net.modificationstation.stationapi.mixin.dimension.server;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.EntityTracker;
import net.minecraft.server.world.ReadOnlyServerWorld;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.storage.WorldStorage;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.dimension.v1.event.registry.DimensionTypeRegistryEvent;
import net.modificationstation.stationapi.api.dimension.v1.registry.DimensionTypeRegistry;
import net.modificationstation.stationapi.api.registry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftServer.class)
class MinecraftServerMixin {
    @Shadow public EntityTracker[] entityTrackers;

    @Shadow public ServerWorld[] worlds;

    @ModifyConstant(
            method = "<init>()V",
            constant = @Constant(intValue = 2)
    )
    private int stationapi_modifyServerEntityTrackersSize(int original) {
        StationAPI.EVENT_BUS.post(new DimensionTypeRegistryEvent());
        return DimensionTypeRegistry.INSTANCE.size();
    }

    @Inject(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/System;nanoTime()J",
                    ordinal = 0,
                    remap = false
            )
    )
    private void stationapi_registerServerEntityTrackers(CallbackInfoReturnable<Boolean> cir) {
        final var registry = DimensionTypeRegistry.INSTANCE;

        if (registry.size() < 3)
            return;

        final var dimensions = registry.stream().mapToInt(registry::getLogicalId).toArray();
        for (int i = 2; i < dimensions.length; i++)
            //noinspection DataFlowIssue
            entityTrackers[i] = new EntityTracker((MinecraftServer) (Object) this, dimensions[i]);
    }

    @ModifyConstant(
            method = "loadWorld",
            constant = @Constant(
                    intValue = 2,
                    ordinal = 0
            )
    )
    private int stationapi_modifyDimensionsSize(int original) {
        return DimensionTypeRegistry.INSTANCE.size();
    }

    @ModifyConstant(
            method = "loadWorld",
            constant = @Constant(
                    intValue = 0,
                    ordinal = 1
            )
    )
    private int stationapi_modifyOverworldId(
            int original,
            @Local(index = 6) int worldIndex
    ) {
        final var registry = DimensionTypeRegistry.INSTANCE;
        return registry.getLogicalId(registry.get(worldIndex));
    }

    @WrapOperation(
            method = "loadWorld",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/world/storage/WorldStorage;Ljava/lang/String;IJLnet/minecraft/world/ServerWorld;)Lnet/minecraft/server/world/ReadOnlyServerWorld;"
            )
    )
    private ReadOnlyServerWorld stationapi_instantiateOtherServerWorld(
            MinecraftServer server, WorldStorage storage, String saveName, int dimension, long seed,
            ServerWorld delegate, Operation<ReadOnlyServerWorld> original,
            @Local(index = 6) int worldIndex
    ) {
        final var registry = DimensionTypeRegistry.INSTANCE;
        return original.call(
                server, storage, saveName, registry.getLogicalId(registry.get(worldIndex)), seed, delegate
        );
    }

    @WrapMethod(method = "getWorld")
    private ServerWorld stationapi_getWorld(int index, Operation<ServerWorld> original) {
        final var registry = DimensionTypeRegistry.INSTANCE;
        return registry.getEntryByLogicalId(index)
                .map(RegistryEntry::value)
                .map(registry::getRawId)
                .map(id -> worlds[id])
                .orElseGet(() -> original.call(index));
    }

    @WrapMethod(method = "getEntityTracker")
    private EntityTracker stationapi_getEntityTracker(int index, Operation<EntityTracker> original) {
        final var registry = DimensionTypeRegistry.INSTANCE;
        return registry.getEntryByLogicalId(index)
                .map(RegistryEntry::value)
                .map(registry::getRawId)
                .map(id -> entityTrackers[id])
                .orElseGet(() -> original.call(index));
    }
}
