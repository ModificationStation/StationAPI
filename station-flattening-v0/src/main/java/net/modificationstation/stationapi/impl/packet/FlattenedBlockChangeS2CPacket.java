package net.modificationstation.stationapi.impl.packet;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockBase;
import net.minecraft.level.Level;
import net.minecraft.network.PacketHandler;
import net.minecraft.packet.play.BlockChange0x35S2CPacket;
import net.modificationstation.stationapi.api.packet.IdentifiablePacket;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.impl.network.StationFlatteningPacketHandler;
import org.jetbrains.annotations.ApiStatus;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

public class FlattenedBlockChangeS2CPacket extends BlockChange0x35S2CPacket implements IdentifiablePacket {

    public static final Identifier PACKET_ID = MODID.id("flattening/block_change");

    public int stateId;

    @ApiStatus.Internal
    public FlattenedBlockChangeS2CPacket() {}

    @Environment(EnvType.SERVER)
    public FlattenedBlockChangeS2CPacket(int x, int y, int z, Level world) {
        this.x = x;
        this.y = y;
        this.z = z;
        stateId = BlockBase.STATE_IDS.getRawId(world.getBlockState(x, y, z));
        metadata = (byte) world.getTileMeta(x, y, z);
    }

    @Override
    public void read(DataInputStream in) {
        try {
            x = in.readInt();
            y = in.readShort();
            z = in.readInt();
            stateId = in.readInt();
            metadata = in.read();
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
            out.write(metadata);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void apply(PacketHandler handler) {
        ((StationFlatteningPacketHandler) handler).onBlockChange(this);
    }

    @Override
    public int length() {
        return 15;
    }

    @Override
    public Identifier getId() {
        return PACKET_ID;
    }
}
