package net.modificationstation.stationapi.impl.packet;

import net.fabricmc.api.ModInitializer;
import net.modificationstation.stationapi.api.registry.PacketTypeRegistry;
import net.modificationstation.stationapi.api.registry.Registry;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

public class StationFlatteningNetworkingImpl implements ModInitializer {

    @Override
    public void onInitialize() {

        // Registering packets for flattened save format
        Registry.register(PacketTypeRegistry.INSTANCE, NAMESPACE)
                .accept("flattening/chunk_data", FlattenedChunkDataS2CPacket.TYPE)
                .accept("flattening/multi_block_change", FlattenedMultiBlockChangeS2CPacket.TYPE)
                .accept("flattening/block_change", FlattenedBlockChangeS2CPacket.TYPE)
                .accept("flattening/chunk_section_data", FlattenedChunkSectionDataS2CPacket.TYPE);
    }
}
