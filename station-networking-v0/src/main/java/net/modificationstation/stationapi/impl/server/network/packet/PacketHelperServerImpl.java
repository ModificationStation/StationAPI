package net.modificationstation.stationapi.impl.server.network.packet;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.MinecraftServer;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.impl.network.packet.PacketHelperImpl;

public class PacketHelperServerImpl extends PacketHelperImpl {
    @Override
    public void send(Packet packet) {
        packet.apply(
                packet instanceof ManagedPacket<?> managedPacket
                        ? managedPacket.getType().getHandler().orElse(null)
                        : null
        );
    }

    @Override
    public void sendTo(PlayerEntity player, Packet packet) {
        ((ServerPlayerEntity) player).networkHandler.sendPacket(packet);
    }

    @Override
    public void dispatchLocallyAndSendTo(PlayerEntity player, Packet packet) {
        send(packet);
        sendTo(player, packet);
    }

    @Override
    public void sendToAllTracking(Entity entity, Packet packet) {
        //noinspection deprecation
        ((MinecraftServer) FabricLoader.getInstance().getGameInstance())
                .getEntityTracker(entity.world.dimension.id).sendToAround(entity, packet);
    }

    @Override
    public void dispatchLocallyAndToAllTracking(Entity entity, Packet packet) {
        send(packet);
        sendToAllTracking(entity, packet);
    }
}
