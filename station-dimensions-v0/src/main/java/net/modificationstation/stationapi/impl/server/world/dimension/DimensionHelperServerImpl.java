package net.modificationstation.stationapi.impl.server.world.dimension;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_467;
import net.minecraft.class_73;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.packet.play.PlayerRespawnPacket;
import net.minecraft.server.MinecraftServer;
import net.modificationstation.stationapi.api.registry.DimensionRegistry;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.impl.world.dimension.DimensionHelperImpl;

import static net.modificationstation.stationapi.api.world.dimension.VanillaDimensions.OVERWORLD;

public class DimensionHelperServerImpl extends DimensionHelperImpl {

    @Override
    public void switchDimension(PlayerEntity player, Identifier destination, double scale, class_467 travelAgent) {
        //noinspection deprecation
        MinecraftServer server = (MinecraftServer) FabricLoader.getInstance().getGameInstance();
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
        class_73 var2 = server.method_2157(serverPlayer.dimensionId);
        DimensionRegistry dimensions = DimensionRegistry.INSTANCE;

        int overworldSerial = dimensions.getLegacyId(OVERWORLD).orElseThrow(() -> new IllegalStateException("Overworld not found!"));
        int destinationSerial = dimensions.getLegacyId(destination).orElseThrow(() -> new IllegalArgumentException("Unknown dimension: " + destination + "!"));

        player.dimensionId = player.dimensionId == destinationSerial ? overworldSerial : destinationSerial;

        class_73 var4 = server.method_2157(serverPlayer.dimensionId);
        serverPlayer.field_255.method_835(new PlayerRespawnPacket((byte)serverPlayer.dimensionId));
        var2.method_236(serverPlayer);
        serverPlayer.dead = false;
        double var5 = serverPlayer.x;
        double var7 = serverPlayer.z;
        if (serverPlayer.dimensionId == destinationSerial) {
            var5 *= scale;
            var7 *= scale;
        } else {
            var5 /= scale;
            var7 /= scale;
        }
        serverPlayer.method_1341(var5, serverPlayer.y, var7, serverPlayer.yaw, serverPlayer.pitch);
        if (serverPlayer.isAlive())
            var2.method_193(serverPlayer, false);

        if (serverPlayer.isAlive()) {
            var4.method_210(serverPlayer);
            serverPlayer.method_1341(var5, serverPlayer.y, var7, serverPlayer.yaw, serverPlayer.pitch);
            var4.method_193(serverPlayer, false);
            var4.field_273.field_933 = true;
            travelAgent.method_1530(var4, serverPlayer);
            var4.field_273.field_933 = false;
        }

        server.field_2842.method_554(serverPlayer);
        serverPlayer.field_255.method_832(serverPlayer.x, serverPlayer.y, serverPlayer.z, serverPlayer.yaw, serverPlayer.pitch);
        serverPlayer.method_1375(var4);
        server.field_2842.method_556(serverPlayer, var4);
        server.field_2842.method_581(serverPlayer);
    }
}
