package net.modificationstation.stationapi.mixin.network;

import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Set;

@Mixin(Packet.class)
public interface AbstractPacketAccessor {
    @Accessor("S2C")
    static Set<Integer> getClientBoundPackets() {
        return Util.assertMixin();
    }

    @Accessor("C2s")
    static Set<Integer> getServerBoundPackets() {
        return Util.assertMixin();
    }

    @Invoker
    static void invokeRegister(int rawId, boolean clientBound, boolean serverBound, Class<? extends Packet> type) {
        Util.assertMixin();
    }
}
