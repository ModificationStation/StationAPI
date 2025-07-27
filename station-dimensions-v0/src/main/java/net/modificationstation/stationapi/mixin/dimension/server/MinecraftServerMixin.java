package net.modificationstation.stationapi.mixin.dimension.server;

import it.unimi.dsi.fastutil.ints.IntArrays;
import it.unimi.dsi.fastutil.ints.IntSortedSet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.EntityTracker;
import net.minecraft.server.world.ReadOnlyServerWorld;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.storage.WorldStorage;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.registry.DimensionRegistryEvent;
import net.modificationstation.stationapi.api.registry.DimensionRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftServer.class)
class MinecraftServerMixin {
    @Shadow public EntityTracker[] field_2847;

    @Shadow public ServerWorld[] field_2841;

    @ModifyConstant(
            method = "<init>()V",
            constant = @Constant(intValue = 2)
    )
    private int stationapi_modifyServerEntityTrackersSize(int original) {
        StationAPI.EVENT_BUS.post(new DimensionRegistryEvent());
        return DimensionRegistry.INSTANCE.serialView.size();
    }

    @Inject(
            method = "method_2166",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/System;nanoTime()J",
                    shift = At.Shift.BEFORE,
                    ordinal = 0,
                    remap = false
            )
    )
    private void stationapi_registerServerEntityTrackers(CallbackInfoReturnable<Boolean> cir) {
        IntSortedSet dimensions = DimensionRegistry.INSTANCE.serialView.keySet();
        int[] otherDimensions = dimensions.tailSet(dimensions.toIntArray()[2]).toIntArray();
        for (int i = 0; i < otherDimensions.length; i++)
            //noinspection DataFlowIssue
            field_2847[i + 2] = new EntityTracker((MinecraftServer) (Object) this, otherDimensions[i]);
    }

    @ModifyConstant(
            method = "method_2159",
            constant = @Constant(
                    intValue = 2,
                    ordinal = 0
            )
    )
    private int stationapi_modifyDimensionsSize(int original) {
        return DimensionRegistry.INSTANCE.serialView.size();
    }

    @ModifyVariable(
            method = "method_2159",
            index = 6,
            at = @At(
                    value = "LOAD",
                    ordinal = 1
            )
    )
    private int stationapi_captureDimensionIndex(int index) {
        return stationapi_capturedIndex = index;
    }

    @Unique
    private int stationapi_capturedIndex;

    @ModifyConstant(
            method = "method_2159",
            constant = @Constant(
                    intValue = 0,
                    ordinal = 1
            )
    )
    private int stationapi_modifyOverworldId(int original) {
        return DimensionRegistry.INSTANCE.serialView.keySet().toIntArray()[stationapi_capturedIndex];
    }

    @Redirect(
            method = "method_2159",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/world/dimension/DimensionData;Ljava/lang/String;IJLnet/minecraft/class_73;)Lnet/minecraft/class_120;"
            )
    )
    private ReadOnlyServerWorld stationapi_instantiateOtherServerWorld(MinecraftServer minecraftServer, WorldStorage arg, String string, int i, long l, ServerWorld arg1) {
        return new ReadOnlyServerWorld(minecraftServer, arg, string, DimensionRegistry.INSTANCE.serialView.keySet().toIntArray()[stationapi_capturedIndex], l, arg1);
    }

    /**
     * @reason There's no point injecting into that code, because I'd have to cancel its entire logic either way.
     * @author mine_diver
     */
    @Overwrite
    public ServerWorld method_2157(int index) {
        return field_2841[IntArrays.binarySearch(DimensionRegistry.INSTANCE.serialView.keySet().toIntArray(), index, DimensionRegistry.DIMENSIONS_COMPARATOR)];
    }

    /**
     * @reason There's no point injecting into that code, because I'd have to cancel its entire logic either way.
     * @author mine_diver
     */
    @Overwrite
    public EntityTracker method_2165(int i) {
        return field_2847[IntArrays.binarySearch(DimensionRegistry.INSTANCE.serialView.keySet().toIntArray(), i, DimensionRegistry.DIMENSIONS_COMPARATOR)];
    }
}
