package net.modificationstation.stationapi.api.world.dimension;

import net.minecraft.class_467;
import net.minecraft.entity.player.PlayerEntity;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.SideUtil;
import net.modificationstation.stationapi.impl.client.level.dimension.DimensionHelperClientImpl;
import net.modificationstation.stationapi.impl.level.dimension.DimensionHelperImpl;
import net.modificationstation.stationapi.impl.server.level.dimension.DimensionHelperServerImpl;

public class DimensionHelper {

    @SuppressWarnings("Convert2MethodRef") // Method references load their target classes on both sides, causing crashes.
    private static final DimensionHelperImpl INSTANCE = SideUtil.get(() -> new DimensionHelperClientImpl(), () -> new DimensionHelperServerImpl());

    public static void switchDimension(PlayerEntity player, Identifier destination, double scale, class_467 travelAgent) {
        INSTANCE.switchDimension(player, destination, scale, travelAgent);
    }
}
