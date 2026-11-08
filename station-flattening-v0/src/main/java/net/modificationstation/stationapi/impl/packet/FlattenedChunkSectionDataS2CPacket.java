package net.modificationstation.stationapi.impl.packet;

import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.impl.network.StationFlatteningPacketHandler;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedChunk;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class FlattenedChunkSectionDataS2CPacket extends Packet implements ManagedPacket<FlattenedChunkSectionDataS2CPacket> {
    public static final PacketType<FlattenedChunkSectionDataS2CPacket> TYPE = PacketType.builder(true, false, FlattenedChunkSectionDataS2CPacket::new).build();

    public int chunkX, chunkZ, sectionIndex;
    private int sectionSize;
    /** the section's size before deflating, on the server */
    private int rawSize;
    public byte[] sectionData;

    @ApiStatus.Internal
    public FlattenedChunkSectionDataS2CPacket() {
        worldPacket = true;
    }

    public FlattenedChunkSectionDataS2CPacket(World world, int chunkX, int chunkZ, int sectionIndex) {
        worldPacket = true;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.sectionIndex = sectionIndex;
        FlattenedChunk chunk = (FlattenedChunk) world.getChunk(chunkX, chunkZ);
        SectionPacketData data = SectionPacketData.of(chunk.sections[sectionIndex]);
        sectionData = data.compressed;
        sectionSize = data.length;
        rawSize = data.rawSize;
    }

    @Override
    public void read(DataInputStream in) {
        try {
            chunkX = in.readInt();
            chunkZ = in.readInt();
            sectionIndex = in.read();
            int realSectionSize = in.readInt();
            int sectionSize = in.readInt();
            byte[] sectionData = new byte[sectionSize];
            in.readFully(sectionData);
            this.sectionData = SectionPacketData.inflate(sectionData, realSectionSize);
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
            out.writeInt(rawSize);
            out.writeInt(sectionSize);
            out.write(sectionData, 0, sectionSize);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void apply(NetworkHandler arg) {
        ((StationFlatteningPacketHandler) arg).onChunkSection(this);
    }

    @Override
    public int size() {
        return 17 + sectionSize;
    }

    @Override
    public @NotNull PacketType<FlattenedChunkSectionDataS2CPacket> getType() {
        return TYPE;
    }
}
