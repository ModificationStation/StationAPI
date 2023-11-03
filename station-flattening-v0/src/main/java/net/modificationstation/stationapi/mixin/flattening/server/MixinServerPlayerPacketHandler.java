package net.modificationstation.stationapi.mixin.flattening.server;

import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.world.World;
import net.modificationstation.stationapi.impl.packet.FlattenedBlockChangeS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayNetworkHandler.class)
public class MixinServerPlayerPacketHandler {
    @Redirect(
            method = {
                    "onPlayerDigging",
                    "onPlaceBlock"
            },
            at = @At(
                    value = "NEW",
                    target = "(IIILnet/minecraft/level/Level;)Lnet/minecraft/packet/play/BlockChange0x35S2CPacket;"
            )
    )
    private BlockUpdateS2CPacket flatten(int x, int y, int z, World world) {
        return new FlattenedBlockChangeS2CPacket(x, y, z, world);
    }
}
