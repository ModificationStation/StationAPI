package net.modificationstation.stationapi.mixin.flattening.server;

import net.minecraft.level.Level;
import net.minecraft.packet.play.BlockChange0x35S2CPacket;
import net.minecraft.server.network.ServerPlayerPacketHandler;
import net.modificationstation.stationapi.impl.packet.StationFlatteningBlockChangeS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayerPacketHandler.class)
public class MixinServerPlayerPacketHandler {

    @SuppressWarnings({"InvalidMemberReference", "UnresolvedMixinReference", "MixinAnnotationTarget", "InvalidInjectorMethodSignature"})
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
    private BlockChange0x35S2CPacket flatten(int x, int y, int z, Level world) {
        return new StationFlatteningBlockChangeS2CPacket(x, y, z, world);
    }
}
