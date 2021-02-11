package net.modificationstation.stationapi.api.common.event.packet;

import net.minecraft.packet.AbstractPacket;
import net.modificationstation.stationapi.api.common.event.GameEventOld;
import uk.co.benjiweber.expressions.functions.QuadConsumer;

import java.util.function.Consumer;

public interface PacketRegister {

    GameEventOld<PacketRegister> EVENT = new GameEventOld<>(PacketRegister.class,
            listeners ->
                    register -> {
                        for (PacketRegister listener : listeners)
                            listener.registerPackets(register);
                    },
            (Consumer<GameEventOld<PacketRegister>>) packetRegister ->
                    packetRegister.register(register -> GameEventOld.EVENT_BUS.post(new Data(register)))
    );

    void registerPackets(QuadConsumer<Integer, Boolean, Boolean, Class<? extends AbstractPacket>> register);

    final class Data extends GameEventOld.Data<PacketRegister> {

        public final QuadConsumer<Integer, Boolean, Boolean, Class<? extends AbstractPacket>> register;

        private Data(QuadConsumer<Integer, Boolean, Boolean, Class<? extends AbstractPacket>> register) {
            super(EVENT);
            this.register = register;
        }

        public void register(int packetId, boolean receivableOnClient, boolean receivableOnServer, Class<? extends AbstractPacket> packetClass) {
            register.accept(packetId, receivableOnClient, receivableOnServer, packetClass);
        }
    }
}
