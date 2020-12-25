package net.modificationstation.stationapi.mixin.common;

import net.minecraft.packet.AbstractPacket;
import net.modificationstation.stationapi.api.common.event.packet.PacketRegister;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractPacket.class)
public class MixinAbstractPacket {

    @Shadow
    static void register(int id, boolean flag, boolean flag1, Class<? extends AbstractPacket> arg) {
    }

    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/packet/AbstractPacket;register(IZZLjava/lang/Class;)V", ordinal = 56, shift = At.Shift.AFTER))
    private static void afterVanillaPackets(CallbackInfo ci) {
        PacketRegister.EVENT.getInvoker().registerPackets(MixinAbstractPacket::register);
    }
}
