package net.modificationstation.stationapi.impl.packet;

import net.minecraft.block.Block;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.s2c.play.ChunkDeltaUpdateS2CPacket;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.impl.network.StationFlatteningPacketHandler;
import net.modificationstation.stationapi.impl.util.math.ChunkSectionPos;
import net.modificationstation.stationapi.impl.world.chunk.ChunkSection;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedChunk;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

public class FlattenedChunkDeltaUpdateS2CPacket extends ChunkDeltaUpdateS2CPacket implements ManagedPacket<FlattenedChunkDeltaUpdateS2CPacket> {
    public static final PacketType<FlattenedChunkDeltaUpdateS2CPacket> TYPE = PacketType.builder(true, false, FlattenedChunkDeltaUpdateS2CPacket::new).build();

    public int sectionIndex;
    public int[] stateRawIds;

    private FlattenedChunkDeltaUpdateS2CPacket() {}

    public FlattenedChunkDeltaUpdateS2CPacket(int chunkX, int chunkZ, int sectionIndex, short[] positions, int arraySize, World world) {
        this.x = chunkX;
        this.z = chunkZ;
        this.sectionIndex = sectionIndex;
        this.size = arraySize;
        this.positions = new short[arraySize];
        stateRawIds = new int[arraySize];
        blockMetadata = new byte[arraySize];
        ChunkSection section = Objects.requireNonNullElse(((FlattenedChunk) world.getChunk(chunkX, chunkZ)).sections[sectionIndex], ChunkSection.EMPTY);
        for (int i = 0; i < arraySize; i++) {
            short position = positions[i];
            this.positions[i] = position;
            int localX = ChunkSectionPos.unpackLocalX(position);
            int localY = ChunkSectionPos.unpackLocalY(position);
            int localZ = ChunkSectionPos.unpackLocalZ(position);
            stateRawIds[i] = Block.STATE_IDS.getRawId(section.getBlockState(localX, localY, localZ));
            blockMetadata[i] = (byte) section.getMeta(localX, localY, localZ);
        }
    }

    @Override
    public void read(DataInputStream in) {
        try {
            x = in.readInt();
            z = in.readInt();
            sectionIndex = in.read();
            size = in.read();
            positions = new short[size];
            stateRawIds = new int[size];
            blockMetadata = new byte[size];
            for (int i = 0; i < size; i++) positions[i] = in.readShort();
            for (int i = 0; i < size; i++) stateRawIds[i] = in.readInt();
            in.readFully(blockMetadata);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(DataOutputStream out) {
        try {
            out.writeInt(x);
            out.writeInt(z);
            out.write(sectionIndex);
            out.write(size);
            for (short position : positions) out.writeShort(position);
            for (int stateRawId : stateRawIds) out.writeInt(stateRawId);
            out.write(blockMetadata);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void apply(NetworkHandler handler) {
        ((StationFlatteningPacketHandler) handler).onChunkDeltaUpdate(this);
    }

    @Override
    public int size() {
        return 10 + size * 7;
    }

    @Override
    public @NotNull PacketType<FlattenedChunkDeltaUpdateS2CPacket> getType() {
        return TYPE;
    }
}
