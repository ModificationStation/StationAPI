package net.modificationstation.stationapi.impl.packet;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import net.modificationstation.stationapi.api.network.packet.IdentifiablePacket;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.impl.item.StationNBTSetter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

public class StationInventoryS2CPacket extends InventoryS2CPacket implements IdentifiablePacket {
    public static final Identifier PACKET_ID = NAMESPACE.id("items/inventory");

    public StationInventoryS2CPacket() {
        super();
    }

    public StationInventoryS2CPacket(int syncId, List<ItemStack> contents) {
        super(syncId, contents);
    }

    @Override
    public void read(DataInputStream stream) {
        try {
            this.syncId = stream.readByte();
            int n = stream.readShort();
            this.contents = new ItemStack[n];
            for (int i = 0; i < n; ++i) {
                short s = stream.readShort();
                if (s < 0) continue;
                byte by = stream.readByte();
                short s2 = stream.readShort();
                ItemStack stack = new ItemStack(s, by, s2);

                if (!stream.readBoolean())
                    StationNBTSetter.cast(contents[i]).setStationNbt((NbtCompound) NbtElement.readTag(stream));

                this.contents[i] = stack;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeByte(this.syncId);
            stream.writeShort(this.contents.length);
            for (int i = 0; i < this.contents.length; ++i) {
                ItemStack stack = this.contents[i];
                if (stack == null) {
                    stream.writeShort(-1);
                    continue;
                }
                stream.writeShort((short) stack.itemId);
                stream.writeByte((byte) stack.count);
                stream.writeShort((short) stack.getDamage());

                NbtCompound stationNbt = stack.getStationNbt();
                boolean empty = stationNbt.values().isEmpty();
                stream.writeBoolean(empty);
                if (empty) continue;
                NbtElement.writeTag(stationNbt, stream);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Identifier getId() {
        return PACKET_ID;
    }
}
