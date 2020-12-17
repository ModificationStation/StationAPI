package net.modificationstation.stationloader.api.common.event.packet;

import net.minecraft.packet.AbstractPacket;
import net.modificationstation.stationloader.api.common.event.GameEvent;
import uk.co.benjiweber.expressions.functions.QuadConsumer;

import java.util.function.Consumer;

public interface PacketRegister {

    GameEvent<PacketRegister> EVENT = new GameEvent<>(PacketRegister.class,
            listeners ->
                    register -> {
                        for (PacketRegister listener : listeners)
                            listener.registerPackets(register);
                    },
            (Consumer<GameEvent<PacketRegister>>) packetRegister ->
                    packetRegister.register(register -> GameEvent.EVENT_BUS.post(new Data(register)))
    );

    void registerPackets(QuadConsumer<Integer, Boolean, Boolean, Class<? extends AbstractPacket>> register);

    final class Data extends GameEvent.Data<PacketRegister> {

        private final QuadConsumer<Integer, Boolean, Boolean, Class<? extends AbstractPacket>> register;

        private Data(QuadConsumer<Integer, Boolean, Boolean, Class<? extends AbstractPacket>> register) {
            super(EVENT);
            this.register = register;
        }

        public void register(int packetId, boolean receivableOnClient, boolean receivableOnServer, Class<? extends AbstractPacket> packetClass) {
            register.accept(packetId, receivableOnClient, receivableOnServer, packetClass);
        }
    }
}
