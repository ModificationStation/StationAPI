package net.modificationstation.stationapi.impl.packet;

import net.fabricmc.api.ModInitializer;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.api.registry.PacketTypeRegistry;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.util.function.BulkBiConsumer;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

public class StationFlatteningNetworkingImpl implements ModInitializer {

    @Override
    public void onInitialize() {

        // Registering packets for flattened save format
        BulkBiConsumer.of((String id, PacketType<?> type) -> Registry.register(PacketTypeRegistry.INSTANCE, NAMESPACE.id(id), type))
                .accept("flattening/chunk_data", FlattenedChunkDataS2CPacket.TYPE)
                .accept("flattening/multi_block_change", FlattenedMultiBlockChangeS2CPacket.TYPE)
                .accept("flattening/block_change", FlattenedBlockChangeS2CPacket.TYPE)
                .accept("flattening/chunk_section_data", FlattenedChunkSectionDataS2CPacket.TYPE);
    }
}
