package net.modificationstation.stationapi.impl.network;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.network.packet.PacketRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.PacketTypeRegistry;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.impl.network.packet.s2c.play.RemapClientRegistryS2CPacket;

import static net.mine_diver.unsafeevents.listener.ListenerPriority.HIGH;
import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE, priority = HIGH)
public final class RegistryNetworkingInit {
    @EventListener
    private static void onInitialize(PacketRegisterEvent event) {
        Registry.register(PacketTypeRegistry.INSTANCE, NAMESPACE.id("registry/remap_client"), RemapClientRegistryS2CPacket.TYPE);
    }
}
