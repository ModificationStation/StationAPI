package net.modificationstation.stationapi.impl.client.network;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.level.ClientLevel;
import net.minecraft.level.chunk.Chunk;
import net.modificationstation.stationapi.impl.level.chunk.ChunkSection;
import net.modificationstation.stationapi.impl.level.chunk.FlattenedChunk;
import net.modificationstation.stationapi.impl.network.StationFlatteningPacketHandler;
import net.modificationstation.stationapi.impl.packet.FlattenedBlockChangeS2CPacket;
import net.modificationstation.stationapi.impl.packet.FlattenedChunkDataS2CPacket;
import net.modificationstation.stationapi.impl.packet.FlattenedMultiBlockChangeS2CPacket;
import net.modificationstation.stationapi.impl.util.math.ChunkSectionPos;

import java.nio.ByteBuffer;

public class FlattenedClientPlayNetworkHandler extends StationFlatteningPacketHandler {

    @Override
    public boolean isServerSide() {
        return false;
    }

    @Override
    public void onMapChunk(FlattenedChunkDataS2CPacket packet) {
        //noinspection deprecation
        ClientLevel world = (ClientLevel) ((Minecraft) FabricLoader.getInstance().getGameInstance()).level;
        int fromX = packet.chunkX << 4;
        int fromZ = packet.chunkZ << 4;
        world.method_1498(fromX, world.getBottomY(), fromZ, fromX + 15, world.getTopY() - 1, fromZ + 15);
        Chunk chunk = world.getChunkFromCache(packet.chunkX, packet.chunkZ);
        if (chunk instanceof FlattenedChunk flatteningChunk) {
            ByteBuffer buf = ByteBuffer.wrap(packet.sectionsData);
            for (int i = 0; i < world.countVerticalSections(); i++)
                flatteningChunk.getOrCreateSection(world.sectionIndexToCoord(i) << 4, true).readDataPacket(buf);
        }
        chunk.method_892();
        world.method_202(fromX, world.getBottomY(), fromZ, fromX + 16, world.getTopY(), fromZ + 16);
    }

    @Override
    public void onBlockChange(FlattenedBlockChangeS2CPacket packet) {
        //noinspection deprecation
        ClientLevel world = (ClientLevel) ((Minecraft) FabricLoader.getInstance().getGameInstance()).level;
        world.method_1498(packet.x, packet.y, packet.z, packet.x, packet.y, packet.z);
        world.setBlockStateWithNotify(packet.x, packet.y, packet.z, BlockBase.STATE_IDS.get(packet.stateId));
        world.setTileMeta(packet.x, packet.y, packet.z, packet.metadata);
    }

    @Override
    public void onMultiBlockChange(FlattenedMultiBlockChangeS2CPacket packet) {
        //noinspection deprecation
        ClientLevel world = (ClientLevel) ((Minecraft) FabricLoader.getInstance().getGameInstance()).level;
        ChunkSectionPos sectionPos = ChunkSectionPos.from(packet.sectionPos);
        Chunk chunk = world.getChunkFromCache(sectionPos.getSectionX(), sectionPos.getSectionZ());
        if (chunk instanceof FlattenedChunk flatteningChunk) {
            ChunkSection section = flatteningChunk.sections[world.sectionCoordToIndex(sectionPos.getSectionY())];

        }
        for (int i = 0; i < arg.arraySize; ++i) {
            short s = arg.coordinateArray[i];
            int n3 = arg.typeArray[i] & 0xFF;
            byte by = arg.metadataArray[i];
            int n4 = s >> 12 & 0xF;
            int n5 = s >> 8 & 0xF;
            int n6 = s & 0xFF;
            chunk.setTileWithMetadata(n4, n6, n5, n3, by);
            this.level.method_1498(n4 + n, n6, n5 + n2, n4 + n, n6, n5 + n2);
            this.level.method_202(n4 + n, n6, n5 + n2, n4 + n, n6, n5 + n2);
        }
    }
}
