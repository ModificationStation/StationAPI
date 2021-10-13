package net.modificationstation.stationapi.impl.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.entity.player.ServerPlayer;
import net.minecraft.server.MinecraftServer;
import net.modificationstation.stationapi.api.util.SideUtils;

public class NetherPortalImpl {

    public static void switchDimension(PlayerBase player) {
        //noinspection Convert2MethodRef
        SideUtils.run(() -> switchDimensionClient(), () -> switchDimensionServer(player));
    }

    @Environment(EnvType.SERVER)
    private static void switchDimensionServer(PlayerBase player) {
        //noinspection deprecation
        ((MinecraftServer) FabricLoader.getInstance().getGameInstance()).serverPlayerConnectionManager.sendToOppositeDimension((ServerPlayer) player);
    }

    @Environment(EnvType.CLIENT)
    private static void switchDimensionClient() {
        //noinspection deprecation
        ((Minecraft) FabricLoader.getInstance().getGameInstance()).switchDimension();
    }
}
