package net.modificationstation.stationapi.impl.network.packet.s2c.play;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.s2c.play.EntityEquipmentUpdateS2CPacket;
import net.modificationstation.stationapi.api.network.packet.IdentifiablePacket;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.impl.network.StationItemsNetworkHandler;
import org.jetbrains.annotations.ApiStatus;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

public class StationEntityEquipmentUpdateS2CPacket extends EntityEquipmentUpdateS2CPacket implements IdentifiablePacket {
    public static final Identifier PACKET_ID = NAMESPACE.id("items/equipment");

    public NbtCompound stationNbt;

    @ApiStatus.Internal
    public StationEntityEquipmentUpdateS2CPacket() {}

    public StationEntityEquipmentUpdateS2CPacket(int id, int slot, ItemStack itemStack) {
        super(id, slot, itemStack);
        this.stationNbt = itemStack == null ? null : itemStack.getStationNbt();
    }

    @Override
    public void read(DataInputStream stream) {
        super.read(stream);
        try {
            if (stream.readBoolean()) return;
            stationNbt = (NbtCompound) NbtCompound.readTag(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(DataOutputStream stream) {
        super.write(stream);
        boolean empty = stationNbt == null || stationNbt.values().isEmpty();
        try {
            stream.writeBoolean(empty);
            if (empty) return;
            NbtElement.writeTag(stationNbt, stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void apply(NetworkHandler networkHandler) {
        ((StationItemsNetworkHandler) networkHandler).onEntityEquipmentUpdate(this);
    }

    @Override
    public Identifier getId() {
        return PACKET_ID;
    }
}
