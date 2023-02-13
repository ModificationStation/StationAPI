package net.modificationstation.stationapi.impl.packet;

import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.block.BlockBase;
import net.minecraft.network.PacketHandler;
import net.minecraft.packet.play.MultiBlockChange0x34S2CPacket;
import net.modificationstation.stationapi.api.packet.IdentifiablePacket;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.impl.level.chunk.ChunkSection;
import net.modificationstation.stationapi.impl.network.StationFlatteningPacketHandler;
import org.jetbrains.annotations.ApiStatus;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

public class FlattenedMultiBlockChangeS2CPacket extends MultiBlockChange0x34S2CPacket implements IdentifiablePacket {

    public static final Identifier PACKET_ID = MODID.id("flattening/multi_block_change");

    public int count;
    public int[] coordinateArray;
    public int[] stateArray;

    @ApiStatus.Internal
    public FlattenedMultiBlockChangeS2CPacket() {}

    public FlattenedMultiBlockChangeS2CPacket(int chunkX, int chunkZ, IntSet positions, ChunkSection section) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        count = positions.size();
        coordinateArray = new int[count];
        stateArray = new int[count];
        metadataArray = new byte[count];
        int i = 0;
        for (int position : positions) {
            coordinateArray[i] = position;
            int localX = position >>> 4 & 0xF;
            int localY = position >>> 8;
            int localZ = position & 0xF;
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
            count = in.read();
            coordinateArray = new int[count];
            stateArray = new int[count];
            metadataArray = new byte[count];
            for (int i = 0; i < count; i++) coordinateArray[i] = in.readInt();
            for (int i = 0; i < count; i++) stateArray[i] = in.readInt();
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
            out.write(count);
            for (int position : coordinateArray) out.writeInt(position);
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
        return 9 + count * 9;
    }

    @Override
    public Identifier getId() {
        return PACKET_ID;
    }
}
