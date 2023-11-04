package net.modificationstation.stationapi.impl.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.modificationstation.stationapi.api.util.SideUtil;

public class NetherPortalImpl {

    public static void switchDimension(PlayerEntity player) {
        //noinspection Convert2MethodRef
        SideUtil.run(() -> switchDimensionClient(), () -> switchDimensionServer(player));
    }

    @Environment(EnvType.SERVER)
    private static void switchDimensionServer(PlayerEntity player) {
        //noinspection deprecation
        ((MinecraftServer) FabricLoader.getInstance().getGameInstance()).field_2842.method_578((ServerPlayerEntity) player);
    }

    @Environment(EnvType.CLIENT)
    private static void switchDimensionClient() {
        //noinspection deprecation
        ((Minecraft) FabricLoader.getInstance().getGameInstance()).method_2139();
    }
}
