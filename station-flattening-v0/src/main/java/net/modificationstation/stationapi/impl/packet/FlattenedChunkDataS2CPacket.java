package net.modificationstation.stationapi.impl.packet;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.packet.IdentifiablePacket;
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

public class FlattenedChunkDataS2CPacket extends ChunkDataS2CPacket implements IdentifiablePacket {

    public static final Identifier PACKET_ID = NAMESPACE.id("flattening/chunk_data");

    public int chunkX, chunkZ;
    private int sectionsSize;
    public byte[] sectionsData;

    @ApiStatus.Internal
    public FlattenedChunkDataS2CPacket() {}

    @Environment(EnvType.SERVER)
    public FlattenedChunkDataS2CPacket(World world, int chunkX, int chunkZ) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        FlattenedChunk chunk = (FlattenedChunk) world.method_214(chunkX, chunkZ);
        ChunkSection[] sections = chunk.sections;
        byte[] sectionsData = new byte[getSectionsPacketSize(chunk)];
        ByteBuffer buf = ByteBuffer.wrap(sectionsData);
        for (ChunkSection section : sections) {
            Objects.requireNonNullElse(section, ChunkSection.EMPTY).toPacket(buf);
        }
        Deflater deflater = new Deflater(Deflater.DEFAULT_COMPRESSION);
        try {
            deflater.setInput(sectionsData);
            deflater.finish();
            this.sectionsData = new byte[sectionsData.length];
            sectionsSize = deflater.deflate(this.sectionsData);
        } finally {
            deflater.end();
        }
    }

    private static int getSectionsPacketSize(FlattenedChunk chunk) {
        int i = 0;
        for (ChunkSection chunkSection : chunk.sections)
            i += Objects.requireNonNullElse(chunkSection, ChunkSection.EMPTY).getPacketSize();
        return i;
    }

    @Override
    public void read(DataInputStream in) {
        try {
            chunkX = in.readInt();
            chunkZ = in.readInt();
            int realSectionsSize = in.readInt();
            int sectionsSize = in.readInt();
            byte[] sectionsData = new byte[sectionsSize];
            in.readFully(sectionsData);
            this.sectionsData = new byte[realSectionsSize];
            Inflater inflater = new Inflater();
            inflater.setInput(sectionsData);
            try {
                inflater.inflate(this.sectionsData);
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
            out.writeInt(sectionsData.length);
            out.writeInt(sectionsSize);
            out.write(sectionsData, 0, sectionsSize);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void apply(NetworkHandler handler) {
        ((StationFlatteningPacketHandler) handler).onMapChunk(this);
    }

    @Override
    public int size() {
        return 16 + sectionsSize;
    }

    @Override
    public Identifier getId() {
        return PACKET_ID;
    }
}
