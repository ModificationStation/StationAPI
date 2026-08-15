package net.modificationstation.sltest.packet;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.network.packet.PacketRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Namespace;

@SuppressWarnings("unused")
public class PacketListener {
    @Entrypoint.Namespace
    public static Namespace NAMESPACE;
    
    @EventListener
    public void registerPackets(PacketRegisterEvent event) {
        event.register(NAMESPACE.id("mine_l_moment"), MineLMomentPacket.TYPE);
    }
        
}
