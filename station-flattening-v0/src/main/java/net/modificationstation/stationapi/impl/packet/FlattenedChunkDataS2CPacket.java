package net.modificationstation.stationapi.impl.packet;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.impl.network.StationFlatteningPacketHandler;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedChunk;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class FlattenedChunkDataS2CPacket extends ChunkDataS2CPacket implements ManagedPacket<FlattenedChunkDataS2CPacket> {
    public static final PacketType<FlattenedChunkDataS2CPacket> TYPE = PacketType.builder(true, false, FlattenedChunkDataS2CPacket::new).build();

    public int chunkX, chunkZ;
    /** {@link #chunkData}'s deflated size, on the server */
    private int chunkDataSize;
    /** {@link #chunkData}'s size before deflating */
    private int rawSize;

    private FlattenedChunkDataS2CPacket() {}

    @Environment(EnvType.SERVER)
    public FlattenedChunkDataS2CPacket(World world, int chunkX, int chunkZ) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        FlattenedChunk chunk = (FlattenedChunk) world.getChunk(chunkX, chunkZ);
        SectionPacketData data = SectionPacketData.of(chunk.sections);
        chunkData = data.compressed;
        chunkDataSize = data.length;
        rawSize = data.rawSize;
    }

    @Override
    public void read(DataInputStream in) {
        try {
            chunkX = in.readInt();
            chunkZ = in.readInt();
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
            out.writeInt(rawSize);
            out.writeInt(chunkDataSize);
            out.write(chunkData, 0, chunkDataSize);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void apply(NetworkHandler handler) {
        ((StationFlatteningPacketHandler) handler).handleChunkData(this);
    }

    @Override
    public int size() {
        return 16 + chunkDataSize;
    }

    @Override
    public @NotNull PacketType<FlattenedChunkDataS2CPacket> getType() {
        return TYPE;
    }
}
