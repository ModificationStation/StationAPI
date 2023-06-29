package net.modificationstation.stationapi.impl.packet;

import net.minecraft.block.BlockBase;
import net.minecraft.level.Level;
import net.minecraft.network.PacketHandler;
import net.minecraft.packet.play.MultiBlockChange0x34S2CPacket;
import net.modificationstation.stationapi.api.packet.IdentifiablePacket;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.impl.level.chunk.ChunkSection;
import net.modificationstation.stationapi.impl.level.chunk.FlattenedChunk;
import net.modificationstation.stationapi.impl.network.StationFlatteningPacketHandler;
import net.modificationstation.stationapi.impl.util.math.ChunkSectionPos;
import org.jetbrains.annotations.ApiStatus;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

public class FlattenedMultiBlockChangeS2CPacket extends MultiBlockChange0x34S2CPacket implements IdentifiablePacket {

    public static final Identifier PACKET_ID = MODID.id("flattening/multi_block_change");

    public int sectionIndex;
    public int[] coordinateArray;
    public int[] stateArray;

    @ApiStatus.Internal
    public FlattenedMultiBlockChangeS2CPacket() {}

    public FlattenedMultiBlockChangeS2CPacket(int chunkX, int chunkZ, int sectionIndex, short[] positions, int arraySize, Level world) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.sectionIndex = sectionIndex;
        this.arraySize = arraySize;
        coordinateArray = new int[arraySize];
        stateArray = new int[arraySize];
        metadataArray = new byte[arraySize];
        ChunkSection section = Objects.requireNonNullElse(((FlattenedChunk) world.getChunkFromCache(chunkX, chunkZ)).sections[sectionIndex], ChunkSection.EMPTY);
        for (int i = 0; i < arraySize; i++) {
            short position = positions[i];
            coordinateArray[i] = position;
            int localX = ChunkSectionPos.unpackLocalX(position);
            int localY = ChunkSectionPos.unpackLocalY(position);
            int localZ = ChunkSectionPos.unpackLocalZ(position);
            stateArray[i] = BlockBase.STATE_IDS.getRawId(section.getBlockState(localX, localY, localZ));
            metadataArray[i] = (byte) section.getMeta(localX, localY, localZ);
            i++;
        }
    }

    @Override
    public void read(DataInputStream in) {
        try {
            chunkX = in.readInt();
            chunkZ = in.readInt();
            sectionIndex = in.read();
            arraySize = in.read();
            coordinateArray = new int[arraySize];
            stateArray = new int[arraySize];
            metadataArray = new byte[arraySize];
            for (int i = 0; i < arraySize; i++) coordinateArray[i] = in.readShort();
            for (int i = 0; i < arraySize; i++) stateArray[i] = in.readInt();
            in.readFully(metadataArray);
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
            out.write(arraySize);
            for (int position : coordinateArray) out.writeShort(position);
            for (int stateId : stateArray) out.writeInt(stateId);
            out.write(metadataArray);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void apply(PacketHandler handler) {
        ((StationFlatteningPacketHandler) handler).onMultiBlockChange(this);
    }

    @Override
    public int length() {
        return 10 + arraySize * 7;
    }

    @Override
    public Identifier getId() {
        return PACKET_ID;
    }
}
