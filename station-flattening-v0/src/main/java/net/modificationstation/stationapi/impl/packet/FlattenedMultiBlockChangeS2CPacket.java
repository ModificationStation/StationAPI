package net.modificationstation.stationapi.impl.packet;

import net.minecraft.block.Block;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.s2c.play.ChunkDeltaUpdateS2CPacket;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.network.packet.IdentifiablePacket;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.impl.level.chunk.ChunkSection;
import net.modificationstation.stationapi.impl.level.chunk.FlattenedChunk;
import net.modificationstation.stationapi.impl.network.StationFlatteningPacketHandler;
import net.modificationstation.stationapi.impl.util.math.ChunkSectionPos;
import org.jetbrains.annotations.ApiStatus;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

public class FlattenedMultiBlockChangeS2CPacket extends ChunkDeltaUpdateS2CPacket implements IdentifiablePacket {

    public static final Identifier PACKET_ID = NAMESPACE.id("flattening/multi_block_change");

    public int sectionIndex;
    public int[] stateArray;

    @ApiStatus.Internal
    public FlattenedMultiBlockChangeS2CPacket() {}

    public FlattenedMultiBlockChangeS2CPacket(int chunkX, int chunkZ, int sectionIndex, short[] positions, int arraySize, World world) {
        this.x = chunkX;
        this.z = chunkZ;
        this.sectionIndex = sectionIndex;
        this.size = arraySize;
        positions = new short[arraySize];
        stateArray = new int[arraySize];
        blockMetadata = new byte[arraySize];
        ChunkSection section = Objects.requireNonNullElse(((FlattenedChunk) world.method_214(chunkX, chunkZ)).sections[sectionIndex], ChunkSection.EMPTY);
        for (int i = 0; i < arraySize; i++) {
            short position = positions[i];
            positions[i] = position;
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
    public Identifier getId() {
        return PACKET_ID;
    }
}
