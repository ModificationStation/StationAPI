package net.modificationstation.stationapi.impl.network.packet;

import net.fabricmc.api.ModInitializer;
import net.modificationstation.stationapi.api.registry.PacketTypeRegistry;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.impl.network.packet.c2s.play.StationClickSlotC2SPacket;
import net.modificationstation.stationapi.impl.network.packet.c2s.play.StationPlayerInteractBlockC2SPacket;
import net.modificationstation.stationapi.impl.network.packet.s2c.play.StationEntityEquipmentUpdateS2CPacket;
import net.modificationstation.stationapi.impl.network.packet.s2c.play.StationInventoryS2CPacket;
import net.modificationstation.stationapi.impl.network.packet.s2c.play.StationItemEntitySpawnS2CPacket;
import net.modificationstation.stationapi.impl.network.packet.s2c.play.StationScreenHandlerSlotUpdateS2CPacket;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

public class StationItemsNetworkingImpl implements ModInitializer {
    @Override
    public void onInitialize() {
        Registry.register(PacketTypeRegistry.INSTANCE, NAMESPACE.id("items/slot"), StationClickSlotC2SPacket.TYPE);
        Registry.register(PacketTypeRegistry.INSTANCE, NAMESPACE.id("items/equipment"), StationEntityEquipmentUpdateS2CPacket.TYPE);
        Registry.register(PacketTypeRegistry.INSTANCE, NAMESPACE.id("items/inventory"), StationInventoryS2CPacket.TYPE);
        Registry.register(PacketTypeRegistry.INSTANCE, NAMESPACE.id("items/entity_spawn"), StationItemEntitySpawnS2CPacket.TYPE);
        Registry.register(PacketTypeRegistry.INSTANCE, NAMESPACE.id("items/interact"), StationPlayerInteractBlockC2SPacket.TYPE);
        Registry.register(PacketTypeRegistry.INSTANCE, NAMESPACE.id("items/slot_update"), StationScreenHandlerSlotUpdateS2CPacket.TYPE);
    }
}
