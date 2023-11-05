package net.modificationstation.stationapi.mixin.flattening.server;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.world.World;
import net.modificationstation.stationapi.impl.packet.FlattenedChunkDataS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayerEntity.class)
class ServerPlayerEntityMixin {
    @Redirect(
            method = "method_313",
            at = @At(
                    value = "NEW",
                    target = "(IIIIIILnet/minecraft/world/World;)Lnet/minecraft/network/packet/s2c/play/ChunkDataS2CPacket;"
            )
    )
    private ChunkDataS2CPacket stationapi_catchParams(int i, int j, int k, int l, int m, int n, World arg) {
        return new FlattenedChunkDataS2CPacket(arg, i >> 4, k >> 4);
    }
}
