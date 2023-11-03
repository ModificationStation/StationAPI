package net.modificationstation.stationapi.mixin.flattening.server;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.world.World;
import net.modificationstation.stationapi.impl.packet.FlattenedChunkDataS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayerEntity.class)
public class MixinServerPlayer {
    @Redirect(
            method = "tick(Z)V",
            at = @At(
                    value = "NEW",
                    target = "(IIIIIILnet/minecraft/level/Level;)Lnet/minecraft/packet/play/MapChunk0x33S2CPacket;"
            )
    )
    private ChunkDataS2CPacket catchParams(int i, int j, int k, int l, int m, int n, World arg) {
        return new FlattenedChunkDataS2CPacket(arg, i >> 4, k >> 4);
    }
}
