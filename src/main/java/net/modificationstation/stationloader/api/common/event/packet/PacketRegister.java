package net.modificationstation.stationloader.api.common.event.packet;

import net.minecraft.packet.AbstractPacket;
import net.modificationstation.stationloader.api.common.event.Event;
import net.modificationstation.stationloader.api.common.factory.EventFactory;
import uk.co.benjiweber.expressions.functions.QuadConsumer;

public interface PacketRegister {

    Event<PacketRegister> EVENT = EventFactory.INSTANCE.newEvent(PacketRegister.class, listeners ->
            register -> {
                for (PacketRegister event : listeners)
                    event.registerPackets(register);
            });

    void registerPackets(QuadConsumer<Integer, Boolean, Boolean, Class<? extends AbstractPacket>> register);
}
