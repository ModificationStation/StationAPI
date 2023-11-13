package net.modificationstation.stationapi.impl.packet;

import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.network.packet.IdentifiablePacket;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.impl.level.chunk.ChunkSection;
import net.modificationstation.stationapi.impl.level.chunk.FlattenedChunk;
import net.modificationstation.stationapi.impl.network.StationFlatteningPacketHandler;
import org.jetbrains.annotations.ApiStatus;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

public class FlattenedChunkSectionDataS2CPacket extends Packet implements IdentifiablePacket {
    public static final Identifier PACKET_ID = NAMESPACE.id("flattening/chunk_section_data");

    public int chunkX, chunkZ, sectionIndex;
    private int sectionSize;
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
        FlattenedChunk chunk = (FlattenedChunk) world.method_214(chunkX, chunkZ);
        ChunkSection section = Objects.requireNonNullElse(chunk.sections[sectionIndex], ChunkSection.EMPTY);
        byte[] sectionData = new byte[section.getPacketSize()];
        ByteBuffer buf = ByteBuffer.wrap(sectionData);
        section.toPacket(buf);
        Deflater deflater = new Deflater(Deflater.DEFAULT_COMPRESSION);
        try {
            deflater.setInput(sectionData);
            deflater.finish();
            this.sectionData = new byte[sectionData.length];
            sectionSize = deflater.deflate(this.sectionData);
        } finally {
            deflater.end();
        }
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
            this.sectionData = new byte[realSectionSize];
            Inflater inflater = new Inflater();
            inflater.setInput(sectionData);
            try {
                inflater.inflate(this.sectionData);
            } catch (DataFormatException dataFormatException) {
                throw new IOException("Bad compressed data format");
            } finally {
                inflater.end();
            }
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
            out.writeInt(sectionData.length);
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
    public Identifier getId() {
        return PACKET_ID;
    }
}
