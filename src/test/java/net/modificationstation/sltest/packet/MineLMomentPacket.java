package net.modificationstation.sltest.packet;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.api.util.SideUtil;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MineLMomentPacket extends Packet implements ManagedPacket<MineLMomentPacket> {
    public static final PacketType<MineLMomentPacket> TYPE = PacketType.builder(true, true, MineLMomentPacket::new).build();
    
    int mineLMoments;

    public MineLMomentPacket(int mineLMoments) {
        this.mineLMoments = mineLMoments;
    }

    public MineLMomentPacket() {
    }

    @Override
    public void read(DataInputStream stream) {
        try {
            mineLMoments = stream.readInt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeInt(mineLMoments);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void apply(NetworkHandler networkHandler) {
        SideUtil.run(() -> handleClient(networkHandler), () -> handleServer(networkHandler));
    }

    @Environment(EnvType.CLIENT)
    public void handleClient(NetworkHandler networkHandler) {
        PlayerEntity player = PlayerHelper.getPlayerFromPacketHandler(networkHandler);
        player.sendMessage(mineLMoments + " Mine L Moments have occurred near you");
    }

    @Environment(EnvType.SERVER)
    public void handleServer(NetworkHandler networkHandler) {
        PlayerEntity player = PlayerHelper.getPlayerFromPacketHandler(networkHandler);
        World world = player.world;

        PacketHelper.sendToAllTracking(player, new MineLMomentPacket((int) world.getTime()));
    }

    @Override
    public int size() {
        return 4;
    }

    @Override
    public @NotNull PacketType<MineLMomentPacket> getType() {
        return TYPE;
    }
}
