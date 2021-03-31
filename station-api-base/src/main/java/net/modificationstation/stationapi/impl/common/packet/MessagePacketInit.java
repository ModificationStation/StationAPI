package net.modificationstation.stationapi.impl.common.packet;

import net.modificationstation.stationapi.api.common.StationAPI;
import net.modificationstation.stationapi.api.common.config.Category;
import net.modificationstation.stationapi.api.common.event.EventListener;
import net.modificationstation.stationapi.api.common.event.ListenerPriority;
import net.modificationstation.stationapi.api.common.event.packet.PacketRegisterEvent;
import net.modificationstation.stationapi.api.common.event.registry.RegistryEvent;
import net.modificationstation.stationapi.api.common.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.common.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.common.packet.Message;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class MessagePacketInit {

    @EventListener(priority = ListenerPriority.HIGH)
    private static void registerPackets(PacketRegisterEvent event) {
        Category networkConfig = StationAPI.CONFIG.getCategory("Network");
        event.register(networkConfig.getProperty("PacketCustomDataID", 254).getIntValue(), true, true, Message.class);
        StationAPI.CONFIG.save();
        StationAPI.EVENT_BUS.post(new RegistryEvent.MessageListeners());
    }
}
