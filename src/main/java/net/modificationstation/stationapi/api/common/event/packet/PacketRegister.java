package net.modificationstation.stationapi.api.common.event.packet;

import lombok.RequiredArgsConstructor;
import net.minecraft.packet.AbstractPacket;
import net.modificationstation.stationapi.api.common.event.Event;
import uk.co.benjiweber.expressions.functions.QuadConsumer;

@RequiredArgsConstructor
public class PacketRegister extends Event {

    public final QuadConsumer<Integer, Boolean, Boolean, Class<? extends AbstractPacket>> register;

    public void register(int packetId, boolean receivableOnClient, boolean receivableOnServer, Class<? extends AbstractPacket> packetClass) {
        register.accept(packetId, receivableOnClient, receivableOnServer, packetClass);
    }
}
