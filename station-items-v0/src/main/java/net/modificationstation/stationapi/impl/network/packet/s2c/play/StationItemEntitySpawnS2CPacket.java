package net.modificationstation.stationapi.impl.network.packet.s2c.play;

import net.minecraft.entity.ItemEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.s2c.play.ItemEntitySpawnS2CPacket;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.impl.network.StationItemsNetworkHandler;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class StationItemEntitySpawnS2CPacket extends ItemEntitySpawnS2CPacket implements ManagedPacket<StationItemEntitySpawnS2CPacket> {
    public static final PacketType<StationItemEntitySpawnS2CPacket> TYPE = PacketType.builder(true, false, StationItemEntitySpawnS2CPacket::new).build();

    public NbtCompound stationNbt;

    private StationItemEntitySpawnS2CPacket() {}

    public StationItemEntitySpawnS2CPacket(ItemEntity itemEntity) {
        super(itemEntity);
        stationNbt = itemEntity.stack.getStationNbt();
    }

    @Override
    public void read(DataInputStream stream) {
        super.read(stream);
        try {
            if (stream.readBoolean()) return;
            stationNbt = (NbtCompound) NbtElement.readTag(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(DataOutputStream stream) {
        super.write(stream);
        boolean empty = stationNbt.values().isEmpty();
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
        ((StationItemsNetworkHandler) networkHandler).onItemEntitySpawn(this);
    }

    @Override
    public @NotNull PacketType<StationItemEntitySpawnS2CPacket> getType() {
        return TYPE;
    }
}
