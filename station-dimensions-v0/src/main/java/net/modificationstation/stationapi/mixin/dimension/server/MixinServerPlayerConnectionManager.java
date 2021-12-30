package net.modificationstation.stationapi.mixin.dimension.server;

import it.unimi.dsi.fastutil.ints.IntArrays;
import it.unimi.dsi.fastutil.ints.IntSortedSet;
import net.minecraft.entity.player.ServerPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerPlayerConnectionManager;
import net.minecraft.server.ServerPlayerView;
import net.modificationstation.stationapi.api.registry.DimensionRegistry;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ServerPlayerConnectionManager.class)
public class MixinServerPlayerConnectionManager {

    @Shadow private ServerPlayerView[] playerViews;

    @ModifyConstant(
            method = "<init>(Lnet/minecraft/server/MinecraftServer;)V",
            constant = @Constant(intValue = 2)
    )
    private int modifyPlayerViewsSize(int original) {
        return DimensionRegistry.INSTANCE.serialView.size();
    }

    @Inject(
            method = "<init>(Lnet/minecraft/server/MinecraftServer;)V",
            at = @At("RETURN"),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void initializePlayerViews(MinecraftServer server, CallbackInfo ci, int var2) {
        IntSortedSet dimensions = DimensionRegistry.INSTANCE.serialView.keySet();
        int[] otherDimensions = dimensions.tailSet(dimensions.toIntArray()[2]).toIntArray();
        for (int i = 0; i < otherDimensions.length; i++)
            playerViews[i + 2] = new ServerPlayerView(server, otherDimensions[i], var2);
    }

    @Inject(
            method = "method_554(Lnet/minecraft/entity/player/ServerPlayer;)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/entity/player/ServerPlayer;dimensionId:I",
                    opcode = Opcodes.GETFIELD,
                    shift = At.Shift.BEFORE,
                    ordinal = 0
            )
    )
    private void addPlayerToDimension(ServerPlayer player, CallbackInfo ci) {
        IntSortedSet dimensions = DimensionRegistry.INSTANCE.serialView.keySet();
        int[] otherDimensions = dimensions.tailSet(dimensions.toIntArray()[2]).toIntArray();
        for (int i = 0; i < otherDimensions.length; i++)
            playerViews[i + 2].addPlayer(player);
    }

    /**
     * @reason There's no point injecting into that code, because I'd have to cancel its entire logic either way.
     * @author mine_diver
     */
    @Overwrite
    private ServerPlayerView getPlayerView(int dimension) {
        return playerViews[IntArrays.binarySearch(DimensionRegistry.INSTANCE.serialView.keySet().toIntArray(), dimension, DimensionRegistry.DIMENSIONS_COMPARATOR)];
    }
}
