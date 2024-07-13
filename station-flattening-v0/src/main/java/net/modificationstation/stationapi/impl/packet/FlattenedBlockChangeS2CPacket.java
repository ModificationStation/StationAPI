package net.modificationstation.stationapi.impl.packet;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.impl.network.StationFlatteningPacketHandler;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class FlattenedBlockChangeS2CPacket extends BlockUpdateS2CPacket implements ManagedPacket<FlattenedBlockChangeS2CPacket> {
    public static final PacketType<FlattenedBlockChangeS2CPacket> TYPE = new PacketType<>(true, false, FlattenedBlockChangeS2CPacket::new);

    public int stateId;

    private FlattenedBlockChangeS2CPacket() {}

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
    public @NotNull PacketType<FlattenedBlockChangeS2CPacket> getType() {
        return TYPE;
    }
}
