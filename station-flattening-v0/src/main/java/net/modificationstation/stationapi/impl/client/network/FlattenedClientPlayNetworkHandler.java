package net.modificationstation.stationapi.impl.client.network;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.level.ClientLevel;
import net.minecraft.level.chunk.Chunk;
import net.modificationstation.stationapi.impl.level.chunk.FlattenedChunk;
import net.modificationstation.stationapi.impl.network.StationFlatteningPacketHandler;
import net.modificationstation.stationapi.impl.packet.FlattenedBlockChangeS2CPacket;
import net.modificationstation.stationapi.impl.packet.FlattenedChunkDataS2CPacket;
import net.modificationstation.stationapi.impl.packet.FlattenedChunkSectionDataS2CPacket;
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
        world.setBlockStateWithMetadataWithNotify(packet.x, packet.y, packet.z, BlockBase.STATE_IDS.get(packet.stateId), packet.metadata);
    }

    @Override
    public void onMultiBlockChange(FlattenedMultiBlockChangeS2CPacket packet) {
        //noinspection deprecation
        ClientLevel world = (ClientLevel) ((Minecraft) FabricLoader.getInstance().getGameInstance()).level;
        Chunk chunk = world.getChunkFromCache(packet.chunkX, packet.chunkZ);
        if (chunk instanceof FlattenedChunk flatteningChunk) {
            flatteningChunk.getOrCreateSection(world.sectionIndexToCoord(packet.sectionIndex), true);
            int
                    x = packet.chunkX * 16,
                    y = world.sectionIndexToCoord(packet.sectionIndex) * 16,
                    z = packet.chunkZ * 16;
            for (int i = 0; i < packet.arraySize; i++) {
                int localX, localY, localZ;
                {
                    short position = packet.coordinateArray[i];
                    localX = ChunkSectionPos.unpackLocalX(position);
                    localY = ChunkSectionPos.unpackLocalY(position);
                    localZ = ChunkSectionPos.unpackLocalZ(position);
                }
                int stateId = packet.stateArray[i];
                byte metadata = packet.metadataArray[i];
                chunk.setBlockStateWithMetadata(localX, y + localY, localZ, BlockBase.STATE_IDS.get(stateId), metadata);
                int
                        updateX = x + localX,
                        updateY = y + localY,
                        updateZ = z + localZ;
                world.method_1498(updateX, updateY, updateZ, updateX, updateY, updateZ);
                world.method_202(updateX, updateY, updateZ, updateX, updateY, updateZ);
            }
        }
    }

    @Override
    public void onChunkSection(FlattenedChunkSectionDataS2CPacket packet) {
        //noinspection deprecation
        ClientLevel world = (ClientLevel) ((Minecraft) FabricLoader.getInstance().getGameInstance()).level;
        int fromX = packet.chunkX << 4;
        int fromY = world.sectionIndexToCoord(packet.sectionIndex) << 4;
        int fromZ = packet.chunkZ << 4;
        world.method_1498(fromX, fromY, fromZ, fromX + 15, fromY + 15, fromZ + 15);
        Chunk chunk = world.getChunkFromCache(packet.chunkX, packet.chunkZ);
        if (chunk instanceof FlattenedChunk flatteningChunk) {
            ByteBuffer buf = ByteBuffer.wrap(packet.sectionData);
            flatteningChunk.getOrCreateSection(fromY, true).readDataPacket(buf);
        }
        chunk.method_892();
        world.method_202(fromX, fromY, fromZ, fromX + 16, fromY + 16, fromZ + 16);
    }
}
