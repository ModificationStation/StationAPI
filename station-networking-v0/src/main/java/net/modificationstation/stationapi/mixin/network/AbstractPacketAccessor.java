package net.modificationstation.stationapi.mixin.network;

import net.minecraft.packet.AbstractPacket;
import net.modificationstation.stationapi.api.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Set;

@Mixin(AbstractPacket.class)
public interface AbstractPacketAccessor {

    @Accessor
    static Set<Integer> getServerToClientPackets() {
        return Util.assertMixin();
    }

    @Accessor
    static Set<Integer> getClientToServerPackets() {
        return Util.assertMixin();
    }

    @Invoker
    static void invokeRegister(int packetId, boolean receivableOnClient, boolean receivableOnServer, Class<? extends AbstractPacket> packetClass) {
        Util.assertMixin();
    }
}
