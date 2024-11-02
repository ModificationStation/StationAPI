package net.modificationstation.stationapi.mixin.network;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.network.packet.PacketRegisterEvent;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.registry.PacketTypeRegistry;
import net.modificationstation.stationapi.api.util.Null;
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
import java.util.Objects;

@Mixin(Packet.class)
abstract class PacketMixin {
    @Shadow
    static void register(int rawId, boolean clientBound, boolean serverBound, Class<? extends Packet> type) {}

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
        if (this instanceof ManagedPacket)
            cir.setReturnValue(ManagedPacket.PACKET_ID);
    }

    @Inject(
            method = "write(Lnet/minecraft/network/packet/Packet;Ljava/io/DataOutputStream;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/packet/Packet;write(Ljava/io/DataOutputStream;)V"
            )
    )
    private static void stationapi_ifIdentifiable(Packet packet, DataOutputStream out, CallbackInfo ci) throws IOException {
        if (packet instanceof ManagedPacket<?> managedPacket)
            out.writeInt(PacketTypeRegistry.INSTANCE.getRawId(managedPacket.getType()));
    }

    @WrapOperation(
            method = "read(Ljava/io/DataInputStream;Z)Lnet/minecraft/network/packet/Packet;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/packet/Packet;create(I)Lnet/minecraft/network/packet/Packet;"
            )
    )
    private static Packet stationapi_ifIdentifiable(int rawId, Operation<Packet> original, DataInputStream stream, boolean bl) throws IOException {
        if (rawId == ManagedPacket.PACKET_ID) {
            var packetType = Objects.requireNonNull(PacketTypeRegistry.INSTANCE.get(stream.readInt()), "Received nonexistent managed packet!");
            if (
                    bl && !packetType.serverBound ||
                            !bl && !packetType.clientBound
            ) throw new IOException("Bad packet id " + packetType.registryEntry.registryKey().getValue());
            return packetType.factory.create();
        }
        return original.call(rawId);
    }
}
