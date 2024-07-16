package net.modificationstation.stationapi.impl.network.packet.s2c.play;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.impl.item.StationNBTSetter;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

public class StationInventoryS2CPacket extends InventoryS2CPacket implements ManagedPacket<StationInventoryS2CPacket> {
    public static final PacketType<StationInventoryS2CPacket> TYPE = PacketType.builder(true, false, StationInventoryS2CPacket::new).build();

    private StationInventoryS2CPacket() {}

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
                    StationNBTSetter.cast(stack).setStationNbt((NbtCompound) NbtElement.readTag(stream));

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
    public @NotNull PacketType<StationInventoryS2CPacket> getType() {
        return TYPE;
    }
}
