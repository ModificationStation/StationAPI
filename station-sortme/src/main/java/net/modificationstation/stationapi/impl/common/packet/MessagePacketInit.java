package net.modificationstation.stationapi.impl.common.packet;

import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.config.Category;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.modificationstation.stationapi.api.common.event.packet.PacketRegisterEvent;
import net.modificationstation.stationapi.api.event.registry.RegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
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
