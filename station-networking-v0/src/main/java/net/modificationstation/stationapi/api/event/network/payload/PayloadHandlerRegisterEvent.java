package net.modificationstation.stationapi.api.event.network.payload;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.mine_diver.unsafeevents.event.EventPhases;
import net.minecraft.network.NetworkHandler;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.network.PacketByteBuf;
import net.modificationstation.stationapi.api.network.PayloadHandler;
import net.modificationstation.stationapi.api.network.packet.Payload;
import net.modificationstation.stationapi.api.network.packet.PayloadType;

import java.util.Map;

@SuperBuilder
@EventPhases(StationAPI.INTERNAL_PHASE)
public class PayloadHandlerRegisterEvent extends Event {
    public final NetworkHandler networkHandler;

    public final Map<PayloadType<PacketByteBuf, ? extends Payload<?>>, PayloadHandler> handlers;

    public <T extends Payload<H>, H extends PayloadHandler> void register(PayloadType<PacketByteBuf, T> type, PayloadHandler handler) {
        handlers.put(type, handler);
    }
}
