package net.modificationstation.stationapi.impl.client.network;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.level.ClientLevel;
import net.minecraft.level.chunk.Chunk;
import net.modificationstation.stationapi.impl.level.chunk.StationFlatteningChunkImpl;
import net.modificationstation.stationapi.impl.network.StationFlatteningPacketHandler;
import net.modificationstation.stationapi.impl.packet.StationFlatteningBlockChangeS2CPacket;
import net.modificationstation.stationapi.impl.packet.StationFlatteningChunkDataS2CPacket;

import java.nio.ByteBuffer;

public class StationClientPlayNetworkHandler extends StationFlatteningPacketHandler {

    @Override
    public boolean isServerSide() {
        return false;
    }

    @Override
    public void onBlockChange(StationFlatteningBlockChangeS2CPacket packet) {
        //noinspection deprecation
        ClientLevel world = (ClientLevel) ((Minecraft) FabricLoader.getInstance().getGameInstance()).level;
        world.method_1498(packet.x, packet.y, packet.z, packet.x, packet.y, packet.z);
        world.setBlockStateWithNotify(packet.x, packet.y, packet.z, BlockBase.STATE_IDS.get(packet.rawId));
        world.setTileMeta(packet.x, packet.y, packet.z, packet.meta);
    }

    @Override
    public void onMapChunk(StationFlatteningChunkDataS2CPacket packet) {
        //noinspection deprecation
        ClientLevel world = (ClientLevel) ((Minecraft) FabricLoader.getInstance().getGameInstance()).level;
        int fromX = packet.chunkX << 4;
        int fromZ = packet.chunkZ << 4;
        world.method_1498(fromX, world.getBottomY(), fromZ, fromX + 15, world.getTopY() - 1, fromZ + 15);
        Chunk chunk = world.getChunkFromCache(packet.chunkX, packet.chunkZ);
        if (chunk instanceof StationFlatteningChunkImpl flatteningChunk) {
            ByteBuffer buf = ByteBuffer.wrap(packet.sectionsData);
            for (int i = 0; i < world.countVerticalSections(); i++)
                flatteningChunk.getOrCreateSection(world.sectionIndexToCoord(i) << 4, true).readDataPacket(buf);
        }
        chunk.method_892();
        world.method_202(fromX, world.getBottomY(), fromZ, fromX + 16, world.getTopY(), fromZ + 16);
    }
}
