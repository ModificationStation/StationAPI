package net.modificationstation.stationapi.mixin.flattening.server;

import net.minecraft.entity.player.ServerPlayer;
import net.minecraft.level.Level;
import net.minecraft.packet.play.MapChunk0x33S2CPacket;
import net.modificationstation.stationapi.impl.packet.FlattenedChunkDataS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayer.class)
public class MixinServerPlayer {
    @Redirect(
            method = "tick(Z)V",
            at = @At(
                    value = "NEW",
                    target = "(IIIIIILnet/minecraft/level/Level;)Lnet/minecraft/packet/play/MapChunk0x33S2CPacket;"
            )
    )
    private MapChunk0x33S2CPacket catchParams(int i, int j, int k, int l, int m, int n, Level arg) {
        return new FlattenedChunkDataS2CPacket(arg, i >> 4, k >> 4);
    }
}
