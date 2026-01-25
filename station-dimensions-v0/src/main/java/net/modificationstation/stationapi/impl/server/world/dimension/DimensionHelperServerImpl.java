package net.modificationstation.stationapi.impl.server.world.dimension;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.packet.play.PlayerRespawnPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.dimension.PortalForcer;
import net.modificationstation.stationapi.api.dimension.v1.DimensionType;
import net.modificationstation.stationapi.api.dimension.v1.registry.DimensionTypeRegistry;
import net.modificationstation.stationapi.impl.world.dimension.DimensionHelperImpl;

import static net.modificationstation.stationapi.api.world.dimension.VanillaDimensions.OVERWORLD;

public class DimensionHelperServerImpl extends DimensionHelperImpl {
    @Override
    public void switchDimension(PlayerEntity player, DimensionType<?> destination, double scale, PortalForcer portalForcer) {
        //noinspection deprecation
        MinecraftServer server = (MinecraftServer) FabricLoader.getInstance().getGameInstance();
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
        ServerWorld var2 = server.getWorld(serverPlayer.dimensionId);
        final var dimensions = DimensionTypeRegistry.INSTANCE;

        int overworldSerial = dimensions.getLogicalId(DimensionTypeRegistry.INSTANCE.get(OVERWORLD));
        int destinationSerial = dimensions.getLogicalId(destination);

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
            portalForcer.moveToPortal(var4, serverPlayer);
            var4.chunkCache.forceLoad = false;
        }

        server.playerManager.updatePlayerAfterDimensionChange(serverPlayer);
        serverPlayer.networkHandler.teleport(serverPlayer.x, serverPlayer.y, serverPlayer.z, serverPlayer.yaw, serverPlayer.pitch);
        serverPlayer.setWorld(var4);
        server.playerManager.sendWorldInfo(serverPlayer, var4);
        server.playerManager.sendPlayerStatus(serverPlayer);
    }
}
