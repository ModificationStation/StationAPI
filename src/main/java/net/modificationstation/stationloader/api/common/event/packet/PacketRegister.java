package net.modificationstation.stationloader.api.common.event.packet;

import net.minecraft.packet.AbstractPacket;
import net.modificationstation.stationloader.api.common.event.SimpleEvent;
import uk.co.benjiweber.expressions.functions.QuadConsumer;

public interface PacketRegister {

    SimpleEvent<PacketRegister> EVENT = new SimpleEvent<>(PacketRegister.class, listeners ->
            register -> {
        for (PacketRegister event : listeners)
            event.registerPackets(register);
    });

    void registerPackets(QuadConsumer<Integer, Boolean, Boolean, Class<? extends AbstractPacket>> register);
}
