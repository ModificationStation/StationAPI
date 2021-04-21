package net.modificationstation.stationapi.api.event.packet;

import lombok.RequiredArgsConstructor;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.packet.AbstractPacket;
import uk.co.benjiweber.expressions.functions.QuadConsumer;

@RequiredArgsConstructor
public class PacketRegisterEvent extends Event {

    public final QuadConsumer<Integer, Boolean, Boolean, Class<? extends AbstractPacket>> register;

    public final void register(int packetId, boolean receivableOnClient, boolean receivableOnServer, Class<? extends AbstractPacket> packetClass) {
        register.accept(packetId, receivableOnClient, receivableOnServer, packetClass);
    }

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
