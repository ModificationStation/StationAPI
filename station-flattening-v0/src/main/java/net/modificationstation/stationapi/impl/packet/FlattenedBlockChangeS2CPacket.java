package net.modificationstation.stationapi.impl.packet;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.packet.IdentifiablePacket;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.impl.network.StationFlatteningPacketHandler;
import org.jetbrains.annotations.ApiStatus;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

public class FlattenedBlockChangeS2CPacket extends BlockUpdateS2CPacket implements IdentifiablePacket {

    public static final Identifier PACKET_ID = NAMESPACE.id("flattening/block_change");

    public int stateId;

    @ApiStatus.Internal
    public FlattenedBlockChangeS2CPacket() {}

    @Environment(EnvType.SERVER)
    public FlattenedBlockChangeS2CPacket(int x, int y, int z, World world) {
        this.x = x;
        this.y = y;
        this.z = z;
        stateId = Block.STATE_IDS.getRawId(world.getBlockState(x, y, z));
        blockMetadata = (byte) world.getBlockMeta(x, y, z);
    }

    @Override
    public void read(DataInputStream in) {
        try {
            x = in.readInt();
            y = in.readShort();
            z = in.readInt();
            stateId = in.readInt();
            blockMetadata = in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(DataOutputStream out) {
        try {
            out.writeInt(x);
            out.writeShort(y);
            out.writeInt(z);
            out.writeInt(stateId);
            out.write(blockMetadata);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void apply(NetworkHandler handler) {
        ((StationFlatteningPacketHandler) handler).onBlockChange(this);
    }

    @Override
    public int size() {
        return 15;
    }

    @Override
    public Identifier getId() {
        return PACKET_ID;
    }
}
