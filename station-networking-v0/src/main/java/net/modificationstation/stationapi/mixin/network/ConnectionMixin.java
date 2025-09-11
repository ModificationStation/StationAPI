package net.modificationstation.stationapi.mixin.network;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.network.Connection;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.network.StationConnection;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.atomic.AtomicBoolean;

@Mixin(Connection.class)
class ConnectionMixin {
    @Unique
    private static final Object STATIONAPI$PACKET_READ_LOCK = new Object();
    @Unique
    private static final AtomicBoolean STATIONAPI$BLOCKNG_PACKET = new AtomicBoolean();

    @WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/lang/Thread;start()V"))
    private void stationapi_preventVanillaThreadFromStarting(Thread instance, Operation<Void> original) {
        if (!((Object) this instanceof StationConnection)) {
            original.call(instance);
        }
    }

    @WrapOperation(
            method = "method_1129",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/packet/Packet;apply(Lnet/minecraft/network/NetworkHandler;)V"
            )
    )
    private void stationapi_ifIdentifiable(Packet instance, NetworkHandler networkHandler, Operation<Void> original) {
        if (instance instanceof ManagedPacket<?> managedPacket) {
            instance.apply(managedPacket.getType().getHandler().orElse(networkHandler));
            if (managedPacket.getType().blocking) {
                synchronized (STATIONAPI$PACKET_READ_LOCK) {
                    STATIONAPI$BLOCKNG_PACKET.set(false);
                    STATIONAPI$PACKET_READ_LOCK.notifyAll();
                }
            }
        } else original.call(instance, networkHandler);
    }

    @Inject(
            method = "method_1139",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;add(Ljava/lang/Object;)Z",
                    shift = At.Shift.AFTER
            )
    )
    private void stationapi_waitOnBlockingPacket(
            CallbackInfoReturnable<Boolean> cir,
            @Local(index = 2) Packet packet
    ) {
        if (!(packet instanceof ManagedPacket<?> managedPacket) || !managedPacket.getType().blocking) return;
        synchronized (STATIONAPI$PACKET_READ_LOCK) {
            STATIONAPI$BLOCKNG_PACKET.set(true);
            while (STATIONAPI$BLOCKNG_PACKET.get()) try {
                STATIONAPI$PACKET_READ_LOCK.wait();
            } catch (InterruptedException ignored) {}
        }
    }
}
