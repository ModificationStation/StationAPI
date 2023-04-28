package net.modificationstation.stationapi.mixin.network;

import net.minecraft.packet.AbstractPacket;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.packet.PacketRegisterEvent;
import net.modificationstation.stationapi.api.packet.IdentifiablePacket;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.util.Null;
import net.modificationstation.stationapi.impl.packet.IdentifiablePacketImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

@Mixin(AbstractPacket.class)
public abstract class MixinAbstractPacket {

    @Shadow
    static void register(int id, boolean flag, boolean flag1, Class<? extends AbstractPacket> arg) {
    }

    @Shadow
    public static void writeString(String string, DataOutputStream dataOutputStream) {
    }

    @Shadow
    public static String readString(DataInputStream dataInputStream, int i) {
        return Null.get();
    }

    @Shadow
    public static AbstractPacket create(int i) {
        return Null.get();
    }

    @Inject(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/packet/AbstractPacket;register(IZZLjava/lang/Class;)V", ordinal = 56, shift = At.Shift.AFTER))
    private static void afterVanillaPackets(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(PacketRegisterEvent.builder().register(MixinAbstractPacket::register).build());
    }

    @Inject(
            method = "getPacketID",
            at = @At("HEAD"),
            cancellable = true
    )
    private void ifIdentifiable(CallbackInfoReturnable<Integer> cir) {
        if (this instanceof IdentifiablePacket)
            cir.setReturnValue(IdentifiablePacket.PACKET_ID);
    }

    @Inject(
            method = "writeToStream",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/packet/AbstractPacket;write(Ljava/io/DataOutputStream;)V"
            )
    )
    private static void ifIdentifiable(AbstractPacket packet, DataOutputStream out, CallbackInfo ci) {
        if (packet instanceof IdentifiablePacket idPacket)
            writeString(idPacket.toString(), out);
    }

    @Redirect(
            method = "readFromStream",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/packet/AbstractPacket;create(I)Lnet/minecraft/packet/AbstractPacket;"
            )
    )
    private static AbstractPacket ifIdentifiable(int id, DataInputStream in, boolean server) throws IOException {
        if (id == IdentifiablePacket.PACKET_ID) {
            Identifier identifier = Identifier.of(readString(in, Short.MAX_VALUE));
            if (
                    server && !IdentifiablePacketImpl.CLIENT_TO_SERVER_PACKETS.contains(identifier) ||
                            !server && !IdentifiablePacketImpl.SERVER_TO_CLIENT_PACKETS.contains(identifier)
            ) throw new IOException("Bad packet id " + identifier);
            return IdentifiablePacket.create(identifier);
        }
        return create(id);
    }
}
