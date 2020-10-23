package net.modificationstation.stationloader.api.common.event.packet;

import net.minecraft.packet.AbstractPacket;
import net.modificationstation.stationloader.api.common.event.Event;
import net.modificationstation.stationloader.api.common.factory.EventFactory;
import net.modificationstation.stationloader.impl.common.packet.CustomData;
import uk.co.benjiweber.expressions.functions.QuadConsumer;

import java.util.Map;
import java.util.function.Consumer;

public interface PacketRegister {

    Event<PacketRegister> EVENT = EventFactory.INSTANCE.newEvent(PacketRegister.class, listeners ->
            (register, customDataPackets) -> {
                for (PacketRegister event : listeners)
                    event.registerPackets(register, customDataPackets);
            });

    void registerPackets(QuadConsumer<Integer, Boolean, Boolean, Class<? extends AbstractPacket>> register, Map<String, Map<String, Consumer<CustomData>>> customDataPackets);
}
