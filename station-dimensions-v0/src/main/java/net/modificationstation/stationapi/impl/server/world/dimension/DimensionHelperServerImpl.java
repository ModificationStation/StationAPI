package net.modificationstation.stationapi.impl.server.world.dimension;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.packet.play.PlayerRespawnPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.dimension.PortalForcer;
import net.modificationstation.stationapi.api.registry.DimensionRegistry;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.impl.world.dimension.DimensionHelperImpl;

import static net.modificationstation.stationapi.api.world.dimension.VanillaDimensions.OVERWORLD;

public class DimensionHelperServerImpl extends DimensionHelperImpl {

    @Override
    public void switchDimension(PlayerEntity player, Identifier destination, double scale, PortalForcer travelAgent) {
        //noinspection deprecation
        MinecraftServer server = (MinecraftServer) FabricLoader.getInstance().getGameInstance();
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
        ServerWorld var2 = server.getWorld(serverPlayer.dimensionId);
        DimensionRegistry dimensions = DimensionRegistry.INSTANCE;

        int overworldSerial = dimensions.getLegacyId(OVERWORLD).orElseThrow(() -> new IllegalStateException("Overworld not found!"));
        int destinationSerial = dimensions.getLegacyId(destination).orElseThrow(() -> new IllegalArgumentException("Unknown dimension: " + destination + "!"));

        player.dimensionId = player.dimensionId == destinationSerial ? overworldSerial : destinationSerial;

        ServerWorld var4 = server.getWorld(serverPlayer.dimensionId);
        serverPlayer.networkHandler.sendPacket(new PlayerRespawnPacket((byte)serverPlayer.dimensionId));
        var2.serverRemove(serverPlayer);
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
        serverPlayer.setPositionAndAnglesKeepPrevAngles(var5, serverPlayer.y, var7, serverPlayer.yaw, serverPlayer.pitch);
        if (serverPlayer.isAlive())
            var2.updateEntity(serverPlayer, false);

        if (serverPlayer.isAlive()) {
            var4.spawnEntity(serverPlayer);
            serverPlayer.setPositionAndAnglesKeepPrevAngles(var5, serverPlayer.y, var7, serverPlayer.yaw, serverPlayer.pitch);
            var4.updateEntity(serverPlayer, false);
            var4.chunkCache.forceLoad = true;
            travelAgent.moveToPortal(var4, serverPlayer);
            var4.chunkCache.forceLoad = false;
        }

        server.playerManager.updatePlayerAfterDimensionChange(serverPlayer);
        serverPlayer.networkHandler.teleport(serverPlayer.x, serverPlayer.y, serverPlayer.z, serverPlayer.yaw, serverPlayer.pitch);
        serverPlayer.setWorld(var4);
        server.playerManager.sendWorldInfo(serverPlayer, var4);
        server.playerManager.sendPlayerStatus(serverPlayer);
    }
}
