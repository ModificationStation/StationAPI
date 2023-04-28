package net.modificationstation.stationapi.impl.packet;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.packet.PacketRegisterEvent;
import net.modificationstation.stationapi.api.event.registry.MessageListenerRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.packet.IdentifiablePacket;
import net.modificationstation.stationapi.mixin.network.AbstractPacketAccessor;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class StationNetworkingInit {

    @EventListener(priority = ListenerPriority.HIGH)
    private static void registerPackets(PacketRegisterEvent event) {

        // Avoid side checks for IdentifiablePacket by numerical ID
        AbstractPacketAccessor.getServerToClientPackets().add(IdentifiablePacket.PACKET_ID);
        AbstractPacketAccessor.getClientToServerPackets().add(IdentifiablePacket.PACKET_ID);

        StationAPI.EVENT_BUS.post(new MessageListenerRegistryEvent());
    }
}
