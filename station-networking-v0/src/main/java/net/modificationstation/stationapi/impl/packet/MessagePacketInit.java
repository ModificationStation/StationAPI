package net.modificationstation.stationapi.impl.packet;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.config.Category;
import net.modificationstation.stationapi.api.event.packet.PacketRegisterEvent;
import net.modificationstation.stationapi.api.event.registry.MessageListenerRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.packet.Message;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class MessagePacketInit {

    @EventListener(priority = ListenerPriority.HIGH)
    private static void registerPackets(PacketRegisterEvent event) {
        Category networkConfig = StationAPI.CONFIG.getCategory("Network");
        event.register(networkConfig.getProperty("PacketCustomDataID", 254).getIntValue(), true, true, Message.class);
        StationAPI.CONFIG.save();
        StationAPI.EVENT_BUS.post(new MessageListenerRegistryEvent());
    }
}
