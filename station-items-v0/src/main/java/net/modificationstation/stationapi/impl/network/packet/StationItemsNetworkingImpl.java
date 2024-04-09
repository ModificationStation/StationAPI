package net.modificationstation.stationapi.impl.network.packet;

import net.fabricmc.api.ModInitializer;
import net.modificationstation.stationapi.api.network.packet.IdentifiablePacket;
import net.modificationstation.stationapi.impl.network.packet.s2c.play.StationEntityEquipmentUpdateS2CPacket;
import net.modificationstation.stationapi.impl.network.packet.s2c.play.StationInventoryS2CPacket;
import net.modificationstation.stationapi.impl.network.packet.s2c.play.StationItemEntitySpawnS2CPacket;
import net.modificationstation.stationapi.impl.network.packet.s2c.play.StationScreenHandlerSlotUpdateS2CPacket;

public class StationItemsNetworkingImpl implements ModInitializer {
    @Override
    public void onInitialize() {
        IdentifiablePacket.register(StationEntityEquipmentUpdateS2CPacket.PACKET_ID, true, false, StationEntityEquipmentUpdateS2CPacket::new);
        IdentifiablePacket.register(StationInventoryS2CPacket.PACKET_ID, true, false, StationInventoryS2CPacket::new);
        IdentifiablePacket.register(StationItemEntitySpawnS2CPacket.PACKET_ID, true, false, StationItemEntitySpawnS2CPacket::new);
        IdentifiablePacket.register(StationScreenHandlerSlotUpdateS2CPacket.PACKET_ID, true, false, StationScreenHandlerSlotUpdateS2CPacket::new);
    }
}
