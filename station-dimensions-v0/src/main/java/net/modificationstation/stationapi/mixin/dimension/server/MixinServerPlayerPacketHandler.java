package net.modificationstation.stationapi.mixin.dimension.server;

import net.minecraft.entity.player.ServerPlayer;
import net.minecraft.server.network.ServerPlayerPacketHandler;
import net.modificationstation.stationapi.api.level.dimension.VanillaDimensions;
import net.modificationstation.stationapi.api.registry.DimensionRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ServerPlayerPacketHandler.class)
public class MixinServerPlayerPacketHandler {

    @Shadow private ServerPlayer serverPlayer;

    @SuppressWarnings("DefaultAnnotationParam")
    @ModifyConstant(
            method = "onRespawn(Lnet/minecraft/packet/play/Respawn0x9C2SPacket;)V",
            constant = @Constant(intValue = 0)
    )
    private int modifyRespawnDimension(int original) {
        return serverPlayer.level.dimension.canPlayerSleep() ? serverPlayer.dimensionId : DimensionRegistry.INSTANCE.getSerialID(VanillaDimensions.OVERWORLD).orElseThrow(() -> new IllegalStateException("Overworld not found!"));
    }
}
