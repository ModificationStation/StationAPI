package net.modificationstation.stationapi.impl.server.level.dimension;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_467;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.entity.player.ServerPlayer;
import net.minecraft.packet.play.Respawn0x9C2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.modificationstation.stationapi.api.registry.DimensionRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.impl.level.dimension.DimensionHelperImpl;

import static net.modificationstation.stationapi.api.level.dimension.VanillaDimensions.OVERWORLD;

public class DimensionHelperServerImpl extends DimensionHelperImpl {

    @Override
    public void switchDimension(PlayerBase player, Identifier destination, double scale, class_467 travelAgent) {
        //noinspection deprecation
        MinecraftServer server = (MinecraftServer) FabricLoader.getInstance().getGameInstance();
        ServerPlayer serverPlayer = (ServerPlayer) player;
        ServerLevel var2 = server.getLevel(serverPlayer.dimensionId);
        DimensionRegistry dimensions = DimensionRegistry.INSTANCE;

        int overworldSerial = dimensions.getLegacyId(OVERWORLD).orElseThrow(() -> new IllegalStateException("Overworld not found!"));
        int destinationSerial = dimensions.getLegacyId(destination).orElseThrow(() -> new IllegalArgumentException("Unknown dimension: " + destination + "!"));

        player.dimensionId = player.dimensionId == destinationSerial ? overworldSerial : destinationSerial;

        ServerLevel var4 = server.getLevel(serverPlayer.dimensionId);
        serverPlayer.packetHandler.send(new Respawn0x9C2SPacket((byte)serverPlayer.dimensionId));
        var2.removeEntityServer(serverPlayer);
        serverPlayer.removed = false;
        double var5 = serverPlayer.x;
        double var7 = serverPlayer.z;
        if (serverPlayer.dimensionId == destinationSerial) {
            var5 *= scale;
            var7 *= scale;
        } else {
            var5 /= scale;
            var7 /= scale;
        }
        serverPlayer.setPositionAndAngles(var5, serverPlayer.y, var7, serverPlayer.yaw, serverPlayer.pitch);
        if (serverPlayer.isAlive())
            var2.method_193(serverPlayer, false);

        if (serverPlayer.isAlive()) {
            var4.spawnEntity(serverPlayer);
            serverPlayer.setPositionAndAngles(var5, serverPlayer.y, var7, serverPlayer.yaw, serverPlayer.pitch);
            var4.method_193(serverPlayer, false);
            var4.serverLevelSource.field_933 = true;
            travelAgent.method_1530(var4, serverPlayer);
            var4.serverLevelSource.field_933 = false;
        }

        server.serverPlayerConnectionManager.method_554(serverPlayer);
        serverPlayer.packetHandler.method_832(serverPlayer.x, serverPlayer.y, serverPlayer.z, serverPlayer.yaw, serverPlayer.pitch);
        serverPlayer.setLevel(var4);
        server.serverPlayerConnectionManager.sendPlayerTime(serverPlayer, var4);
        server.serverPlayerConnectionManager.method_581(serverPlayer);
    }
}
