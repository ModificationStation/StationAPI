package net.modificationstation.stationapi.impl.network.packet.s2c.play;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.impl.item.StationNBTSetter;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class StationScreenHandlerSlotUpdateS2CPacket extends ScreenHandlerSlotUpdateS2CPacket implements ManagedPacket<StationScreenHandlerSlotUpdateS2CPacket> {
    public static final PacketType<StationScreenHandlerSlotUpdateS2CPacket> TYPE = PacketType.builder(true, false, StationScreenHandlerSlotUpdateS2CPacket::new).build();

    private StationScreenHandlerSlotUpdateS2CPacket() {}

    public StationScreenHandlerSlotUpdateS2CPacket(int syncId, int slot, ItemStack stack) {
        super(syncId, slot, stack);
    }

    @Override
    public void read(DataInputStream stream) {
        try {
            this.syncId = stream.readByte();
            this.slot = stream.readShort();
            short var2 = stream.readShort();
            if (var2 >= 0) {
                byte var3 = stream.readByte();
                short var4 = stream.readShort();
                this.stack = new ItemStack(var2, var3, var4);

                if (stream.readBoolean()) return;
                StationNBTSetter.cast(stack).setStationNbt((NbtCompound) NbtElement.readTag(stream));
            } else {
                this.stack = null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeByte(this.syncId);
            stream.writeShort(this.slot);
            if (this.stack == null) {
                stream.writeShort(-1);
            } else {
                stream.writeShort(this.stack.itemId);
                stream.writeByte(this.stack.count);
                stream.writeShort(this.stack.getDamage());

                NbtCompound stationNbt = stack.getStationNbt();
                boolean empty = stationNbt.values().isEmpty();
                stream.writeBoolean(empty);
                if (empty) return;
                NbtElement.writeTag(stationNbt, stream);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @NotNull PacketType<StationScreenHandlerSlotUpdateS2CPacket> getType() {
        return TYPE;
    }
}
