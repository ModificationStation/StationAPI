package net.modificationstation.stationapi.impl.packet;

import it.unimi.dsi.fastutil.shorts.ShortSet;
import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.packet.Message;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.impl.level.chunk.ChunkSection;
import net.modificationstation.stationapi.impl.util.math.ChunkSectionPos;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

public class StationFlatteningMultiBlockChangeS2CPacket extends Message {

    public static final Identifier MESSAGE_ID = MODID.id("flattening/multi_block_change");

    public StationFlatteningMultiBlockChangeS2CPacket(ChunkSectionPos sectionPos, ShortSet positions, ChunkSection section) {
        super(MESSAGE_ID);
        longs = new long[] { sectionPos.asLong() };
        int size = positions.size();
        shorts = new short[size];
        ints = new int[size];
        bytes = new byte[size];
        int i = 0;
        for (short position : positions) {
            shorts[i] = position;
            int localX = ChunkSectionPos.unpackLocalX(position);
            int localY = ChunkSectionPos.unpackLocalY(position);
            int localZ = ChunkSectionPos.unpackLocalZ(position);
            ints[i] = BlockBase.STATE_IDS.getRawId(section.getBlockState(localX, localY, localZ));
            bytes[i] = (byte) section.getMeta(localX, localY, localZ);
            i++;
        }
    }
}
