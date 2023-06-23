package net.modificationstation.stationapi.impl.packet;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.packet.PacketRegisterEvent;
import net.modificationstation.stationapi.api.event.registry.MessageListenerRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.packet.Message;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public class MessagePacketInit {

    @EventListener
    private static void registerPackets(PacketRegisterEvent event) {
        event.register(254, true, true, Message.class);
        StationAPI.EVENT_BUS.post(new MessageListenerRegistryEvent());
    }
}
