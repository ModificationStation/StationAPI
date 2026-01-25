package net.modificationstation.stationapi.api.world.dimension;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.dimension.PortalForcer;
import net.modificationstation.stationapi.api.dimension.v1.DimensionType;
import net.modificationstation.stationapi.api.util.SideUtil;
import net.modificationstation.stationapi.impl.client.world.dimension.DimensionHelperClientImpl;
import net.modificationstation.stationapi.impl.server.world.dimension.DimensionHelperServerImpl;
import net.modificationstation.stationapi.impl.world.dimension.DimensionHelperImpl;

public class DimensionHelper {
    @SuppressWarnings("Convert2MethodRef") // Method references load their target classes on both sides, causing crashes.
    private static final DimensionHelperImpl INSTANCE = SideUtil.get(() -> new DimensionHelperClientImpl(), () -> new DimensionHelperServerImpl());

    public static void switchDimension(PlayerEntity player, DimensionType<?> destination, double scale, PortalForcer portalForcer) {
        INSTANCE.switchDimension(player, destination, scale, portalForcer);
    }
}
