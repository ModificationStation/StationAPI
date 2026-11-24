package net.modificationstation.stationapi.impl.packet;

import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.impl.network.StationFlatteningPacketHandler;
import net.modificationstation.stationapi.impl.world.chunk.ChunkSection;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedChunk;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * The blocks in a box of one section.
 * <p>
 * When 10 or more blocks in a chunk change in one tick, vanilla resends a box around all of them with a chunk data
 * packet. We send that box per section instead: one of these for each section the box spans (see
 * {@code FlattenedTrackedChunk}). The payload is {@link ChunkSection#writeBox}.
 */
public class FlattenedChunkSectionDataS2CPacket extends Packet implements ManagedPacket<FlattenedChunkSectionDataS2CPacket> {
    public static final PacketType<FlattenedChunkSectionDataS2CPacket> TYPE = PacketType.builder(true, false, FlattenedChunkSectionDataS2CPacket::new).build();

    public int chunkX, chunkZ, sectionIndex;
    /** the box in section coordinates, bounds inclusive */
    public int minX, minY, minZ, maxX, maxY, maxZ;
    /** {@link #chunkData}'s deflated size, on the server */
    private int chunkDataSize;
    /** {@link #chunkData}'s size before deflating */
    private int rawSize;
    /** the box, see {@link ChunkSection#writeBox}: deflated on the server, inflated on the client */
    public byte[] chunkData;

    @ApiStatus.Internal
    public FlattenedChunkSectionDataS2CPacket() {
        worldPacket = true;
    }

    public FlattenedChunkSectionDataS2CPacket(World world, int chunkX, int chunkZ, int sectionIndex, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        worldPacket = true;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.sectionIndex = sectionIndex;
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        ChunkSection section = Objects.requireNonNullElse(((FlattenedChunk) world.getChunk(chunkX, chunkZ)).sections[sectionIndex], ChunkSection.EMPTY);
        byte[] box = section.writeBox(minX, minY, minZ, maxX, maxY, maxZ);
        SectionPacketData data = SectionPacketData.deflate(box, box.length, SectionPacketData.BOX_LEVEL);
        chunkData = data.compressed;
        chunkDataSize = data.length;
        rawSize = data.rawSize;
    }

    @Override
    public void read(DataInputStream in) {
        try {
            chunkX = in.readInt();
            chunkZ = in.readInt();
            sectionIndex = in.read();
            int x = in.read(), y = in.read(), z = in.read();
            minX = x >> 4;
            maxX = x & 15;
            minY = y >> 4;
            maxY = y & 15;
            minZ = z >> 4;
            maxZ = z & 15;
            if (minX > maxX || minY > maxY || minZ > maxZ)
                throw new IOException("Invalid box " + minX + "," + minY + "," + minZ + " to " + maxX + "," + maxY + "," + maxZ);
            int rawSize = in.readInt();
            int chunkDataSize = in.readInt();
            byte[] chunkData = new byte[chunkDataSize];
            in.readFully(chunkData);
            this.chunkData = SectionPacketData.inflate(chunkData, rawSize);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(DataOutputStream out) {
        try {
            out.writeInt(chunkX);
            out.writeInt(chunkZ);
            out.write(sectionIndex);
            out.write(minX << 4 | maxX);
            out.write(minY << 4 | maxY);
            out.write(minZ << 4 | maxZ);
            out.writeInt(rawSize);
            out.writeInt(chunkDataSize);
            out.write(chunkData, 0, chunkDataSize);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void apply(NetworkHandler handler) {
        ((StationFlatteningPacketHandler) handler).handleChunkSectionData(this);
    }

    @Override
    public int size() {
        return 20 + chunkDataSize;
    }

    @Override
    public @NotNull PacketType<FlattenedChunkSectionDataS2CPacket> getType() {
        return TYPE;
    }
}
