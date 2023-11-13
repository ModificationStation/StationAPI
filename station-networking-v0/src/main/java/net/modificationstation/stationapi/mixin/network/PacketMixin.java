package net.modificationstation.stationapi.mixin.network;

import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.network.packet.PacketRegisterEvent;
import net.modificationstation.stationapi.api.network.packet.IdentifiablePacket;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Null;
import net.modificationstation.stationapi.impl.network.packet.IdentifiablePacketImpl;
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

@Mixin(Packet.class)
abstract class PacketMixin {
    @Shadow
    static void register(int rawId, boolean clientBound, boolean serverBound, Class<? extends Packet> type) {}

    @Shadow
    public static void writeString(String string, DataOutputStream dataOutputStream) {}

    @Shadow
    public static String readString(DataInputStream dataInputStream, int i) {
        return Null.get();
    }

    @Shadow
    public static Packet create(int i) {
        return Null.get();
    }

    @Inject(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/Packet;register(IZZLjava/lang/Class;)V", ordinal = 56, shift = At.Shift.AFTER))
    private static void stationapi_afterVanillaPackets(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(PacketRegisterEvent.builder().register(PacketMixin::register).build());
    }

    @Inject(
            method = "getRawId",
            at = @At("HEAD"),
            cancellable = true
    )
    private void stationapi_ifIdentifiable(CallbackInfoReturnable<Integer> cir) {
        if (this instanceof IdentifiablePacket)
            cir.setReturnValue(IdentifiablePacket.PACKET_ID);
    }

    @Inject(
            method = "write(Lnet/minecraft/network/packet/Packet;Ljava/io/DataOutputStream;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/packet/Packet;write(Ljava/io/DataOutputStream;)V"
            )
    )
    private static void stationapi_ifIdentifiable(Packet packet, DataOutputStream out, CallbackInfo ci) {
        if (packet instanceof IdentifiablePacket idPacket)
            writeString(idPacket.getId().toString(), out);
    }

    @Redirect(
            method = "read(Ljava/io/DataInputStream;Z)Lnet/minecraft/network/packet/Packet;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/packet/Packet;create(I)Lnet/minecraft/network/packet/Packet;"
            )
    )
    private static Packet stationapi_ifIdentifiable(int id, DataInputStream in, boolean server) throws IOException {
        if (id == IdentifiablePacket.PACKET_ID) {
            Identifier identifier = Identifier.of(readString(in, Short.MAX_VALUE));
            if (
                    server && !IdentifiablePacketImpl.SERVER_BOUND_PACKETS.contains(identifier) ||
                            !server && !IdentifiablePacketImpl.CLIENT_BOUND_PACKETS.contains(identifier)
            ) throw new IOException("Bad packet id " + identifier);
            return IdentifiablePacket.create(identifier);
        }
        return create(id);
    }
}
