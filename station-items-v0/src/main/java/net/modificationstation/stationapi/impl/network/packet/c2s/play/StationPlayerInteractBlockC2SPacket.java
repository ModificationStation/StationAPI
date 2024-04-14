package net.modificationstation.stationapi.impl.network.packet.c2s.play;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.modificationstation.stationapi.api.network.packet.IdentifiablePacket;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.impl.item.StationNBTSetter;
import org.jetbrains.annotations.ApiStatus;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

public class StationPlayerInteractBlockC2SPacket extends PlayerInteractBlockC2SPacket implements IdentifiablePacket {
    public static final Identifier PACKET_ID = NAMESPACE.id("items/interact");

    @ApiStatus.Internal
    public StationPlayerInteractBlockC2SPacket() {}

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
    public Identifier getId() {
        return PACKET_ID;
    }
}
