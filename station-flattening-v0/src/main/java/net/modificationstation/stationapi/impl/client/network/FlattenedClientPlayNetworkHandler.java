package net.modificationstation.stationapi.impl.client.network;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.class_454;
import net.minecraft.client.Minecraft;
import net.minecraft.world.chunk.Chunk;
import net.modificationstation.stationapi.impl.network.StationFlatteningPacketHandler;
import net.modificationstation.stationapi.impl.packet.FlattenedBlockChangeS2CPacket;
import net.modificationstation.stationapi.impl.packet.FlattenedChunkDataS2CPacket;
import net.modificationstation.stationapi.impl.packet.FlattenedChunkSectionDataS2CPacket;
import net.modificationstation.stationapi.impl.packet.FlattenedMultiBlockChangeS2CPacket;
import net.modificationstation.stationapi.impl.util.math.ChunkSectionPos;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedChunk;

import java.nio.ByteBuffer;

public class FlattenedClientPlayNetworkHandler extends StationFlatteningPacketHandler {

    @Override
    public boolean isServerSide() {
        return false;
    }

    @Override
    public void onMapChunk(FlattenedChunkDataS2CPacket packet) {
        //noinspection deprecation
        class_454 world = (class_454) ((Minecraft) FabricLoader.getInstance().getGameInstance()).world;
        int fromX = packet.chunkX << 4;
        int fromZ = packet.chunkZ << 4;
        world.method_1498(fromX, world.getBottomY(), fromZ, fromX + 15, world.getTopY() - 1, fromZ + 15);
        Chunk chunk = world.method_214(packet.chunkX, packet.chunkZ);
        if (chunk instanceof FlattenedChunk flatteningChunk) {
            ByteBuffer buf = ByteBuffer.wrap(packet.sectionsData);
            for (int i = 0; i < world.countVerticalSections(); i++)
                flatteningChunk.getOrCreateSection(world.sectionIndexToCoord(i) << 4, true).readDataPacket(buf);
        }
        chunk.populateHeightmap();
        world.method_202(fromX, world.getBottomY(), fromZ, fromX + 16, world.getTopY(), fromZ + 16);
    }

    @Override
    public void onBlockChange(FlattenedBlockChangeS2CPacket packet) {
        //noinspection deprecation
        class_454 world = (class_454) ((Minecraft) FabricLoader.getInstance().getGameInstance()).world;
        world.method_1498(packet.x, packet.y, packet.z, packet.x, packet.y, packet.z);
        world.setBlockStateWithMetadataWithNotify(packet.x, packet.y, packet.z, Block.STATE_IDS.get(packet.stateId), packet.blockMetadata);
    }

    @Override
    public void onMultiBlockChange(FlattenedMultiBlockChangeS2CPacket packet) {
        //noinspection deprecation
        class_454 world = (class_454) ((Minecraft) FabricLoader.getInstance().getGameInstance()).world;
        Chunk chunk = world.method_214(packet.x, packet.z);
        if (chunk instanceof FlattenedChunk flatteningChunk) {
            flatteningChunk.getOrCreateSection(world.sectionIndexToCoord(packet.sectionIndex), true);
            int
                    x = packet.x * 16,
                    y = world.sectionIndexToCoord(packet.sectionIndex) * 16,
                    z = packet.z * 16;
            for (int i = 0; i < packet.size; i++) {
                int localX, localY, localZ;
                {
                    short position = packet.positions[i];
                    localX = ChunkSectionPos.unpackLocalX(position);
                    localY = ChunkSectionPos.unpackLocalY(position);
                    localZ = ChunkSectionPos.unpackLocalZ(position);
                }
                int stateId = packet.stateArray[i];
                byte metadata = packet.blockMetadata[i];
                chunk.setBlockStateWithMetadata(localX, y + localY, localZ, Block.STATE_IDS.get(stateId), metadata);
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
        class_454 world = (class_454) ((Minecraft) FabricLoader.getInstance().getGameInstance()).world;
        int fromX = packet.chunkX << 4;
        int fromY = world.sectionIndexToCoord(packet.sectionIndex) << 4;
        int fromZ = packet.chunkZ << 4;
        world.method_1498(fromX, fromY, fromZ, fromX + 15, fromY + 15, fromZ + 15);
        Chunk chunk = world.method_214(packet.chunkX, packet.chunkZ);
        if (chunk instanceof FlattenedChunk flatteningChunk) {
            ByteBuffer buf = ByteBuffer.wrap(packet.sectionData);
            flatteningChunk.getOrCreateSection(fromY, true).readDataPacket(buf);
        }
        chunk.populateHeightmap();
        world.method_202(fromX, fromY, fromZ, fromX + 16, fromY + 16, fromZ + 16);
    }
}
