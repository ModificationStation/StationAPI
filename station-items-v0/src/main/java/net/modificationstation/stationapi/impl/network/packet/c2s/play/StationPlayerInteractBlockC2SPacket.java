package net.modificationstation.stationapi.impl.network.packet.c2s.play;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.impl.item.StationNBTSetter;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class StationPlayerInteractBlockC2SPacket extends PlayerInteractBlockC2SPacket implements ManagedPacket<StationPlayerInteractBlockC2SPacket> {
    public static final PacketType<StationPlayerInteractBlockC2SPacket> TYPE = new PacketType<>(false, true, StationPlayerInteractBlockC2SPacket::new);

    private StationPlayerInteractBlockC2SPacket() {}

    public StationPlayerInteractBlockC2SPacket(int x, int y, int z, int side, ItemStack stack) {
        super(x, y, z, side, stack);
    }

    @Override
    public void read(DataInputStream stream) {
        super.read(stream);
        if (stack == null) return;
        try {
            if (stream.readBoolean()) return;
            StationNBTSetter.cast(stack).setStationNbt((NbtCompound) NbtElement.readTag(stream));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(DataOutputStream stream) {
        super.write(stream);
        if (stack == null) return;
        boolean empty = stack.getStationNbt().values().isEmpty();
        try {
            stream.writeBoolean(empty);
            if (empty) return;
            NbtElement.writeTag(stack.getStationNbt(), stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @NotNull PacketType<StationPlayerInteractBlockC2SPacket> getType() {
        return TYPE;
    }
}
