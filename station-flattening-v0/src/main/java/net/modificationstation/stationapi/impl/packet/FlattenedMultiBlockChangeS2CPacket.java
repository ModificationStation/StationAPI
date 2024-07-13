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

public class FlattenedMultiBlockChangeS2CPacket extends ChunkDeltaUpdateS2CPacket implements ManagedPacket<FlattenedMultiBlockChangeS2CPacket> {
    public static final PacketType<FlattenedMultiBlockChangeS2CPacket> TYPE = new PacketType<>(true, false, FlattenedMultiBlockChangeS2CPacket::new);

    public int sectionIndex;
    public int[] stateArray;

    private FlattenedMultiBlockChangeS2CPacket() {}

    public FlattenedMultiBlockChangeS2CPacket(int chunkX, int chunkZ, int sectionIndex, short[] positions, int arraySize, World world) {
        this.x = chunkX;
        this.z = chunkZ;
        this.sectionIndex = sectionIndex;
        this.size = arraySize;
        this.positions = new short[arraySize];
        stateArray = new int[arraySize];
        blockMetadata = new byte[arraySize];
        ChunkSection section = Objects.requireNonNullElse(((FlattenedChunk) world.method_214(chunkX, chunkZ)).sections[sectionIndex], ChunkSection.EMPTY);
        for (int i = 0; i < arraySize; i++) {
            short position = positions[i];
            this.positions[i] = position;
            int localX = ChunkSectionPos.unpackLocalX(position);
            int localY = ChunkSectionPos.unpackLocalY(position);
            int localZ = ChunkSectionPos.unpackLocalZ(position);
            stateArray[i] = Block.STATE_IDS.getRawId(section.getBlockState(localX, localY, localZ));
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
            stateArray = new int[size];
            blockMetadata = new byte[size];
            for (int i = 0; i < size; i++) positions[i] = in.readShort();
            for (int i = 0; i < size; i++) stateArray[i] = in.readInt();
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
            for (int stateId : stateArray) out.writeInt(stateId);
            out.write(blockMetadata);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void apply(NetworkHandler handler) {
        ((StationFlatteningPacketHandler) handler).onMultiBlockChange(this);
    }

    @Override
    public int size() {
        return 10 + size * 7;
    }

    @Override
    public @NotNull PacketType<FlattenedMultiBlockChangeS2CPacket> getType() {
        return TYPE;
    }
}
