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

public class FlattenedBlockUpdateS2CPacket extends BlockUpdateS2CPacket implements ManagedPacket<FlattenedBlockUpdateS2CPacket> {
    public static final PacketType<FlattenedBlockUpdateS2CPacket> TYPE = PacketType.builder(true, false, FlattenedBlockUpdateS2CPacket::new).build();

    public int stateRawId;

    private FlattenedBlockUpdateS2CPacket() {}

    @Environment(EnvType.SERVER)
    public FlattenedBlockUpdateS2CPacket(int x, int y, int z, World world) {
        this.x = x;
        this.y = y;
        this.z = z;
        stateRawId = Block.STATE_IDS.getRawId(world.getBlockState(x, y, z));
        blockMetadata = (byte) world.getBlockMeta(x, y, z);
    }

    @Override
    public void read(DataInputStream in) {
        try {
            x = in.readInt();
            y = in.readShort();
            z = in.readInt();
            stateRawId = in.readInt();
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
            out.writeInt(stateRawId);
            out.write(blockMetadata);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void apply(NetworkHandler handler) {
        ((StationFlatteningPacketHandler) handler).onBlockUpdate(this);
    }

    @Override
    public int size() {
        return 15;
    }

    @Override
    public @NotNull PacketType<FlattenedBlockUpdateS2CPacket> getType() {
        return TYPE;
    }
}
