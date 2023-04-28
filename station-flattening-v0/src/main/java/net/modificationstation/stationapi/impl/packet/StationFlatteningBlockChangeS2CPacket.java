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

public class StationFlatteningBlockChangeS2CPacket extends BlockChange0x35S2CPacket implements IdentifiablePacket {

    public static final Identifier PACKET_ID = MODID.id("flattening/block_change");

    public int x, y, z,
            rawId,
            meta;

    @ApiStatus.Internal
    public StationFlatteningBlockChangeS2CPacket() {}

    @Environment(EnvType.SERVER)
    public StationFlatteningBlockChangeS2CPacket(int x, int y, int z, Level world) {
        this.x = x;
        this.y = y;
        this.z = z;
        rawId = BlockBase.STATE_IDS.getRawId(world.getBlockState(x, y, z));
        meta = (byte) world.getTileMeta(x, y, z);
    }

    @Override
    public void read(DataInputStream in) {
        try {
            x = in.readInt();
            y = in.readInt();
            z = in.readInt();
            rawId = in.readInt();
            meta = in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(DataOutputStream out) {
        try {
            out.writeInt(x);
            out.writeInt(y);
            out.writeInt(z);
            out.writeInt(rawId);
            out.write(meta);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void apply(PacketHandler arg) {
        ((StationFlatteningPacketHandler) arg).onBlockChange(this);
    }

    @Override
    public int length() {
        return 17;
    }

    @Override
    public Identifier getId() {
        return PACKET_ID;
    }
}
