package net.modificationstation.stationapi.api.level.dimension;

import net.minecraft.class_467;
import net.minecraft.entity.player.PlayerBase;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.util.SideUtils;
import net.modificationstation.stationapi.impl.client.level.dimension.DimensionHelperClientImpl;
import net.modificationstation.stationapi.impl.level.dimension.DimensionHelperImpl;
import net.modificationstation.stationapi.impl.server.level.dimension.DimensionHelperServerImpl;

public class DimensionHelper {

    @SuppressWarnings("Convert2MethodRef") // Method references load their target classes on both sides, causing crashes.
    private static final DimensionHelperImpl INSTANCE = SideUtils.get(() -> new DimensionHelperClientImpl(), () -> new DimensionHelperServerImpl());

    public static void switchDimension(PlayerBase player, Identifier destination, double scale, class_467 travelAgent, String enteringMessage, String leavingMessage) {
        INSTANCE.switchDimension(player, destination, scale, travelAgent, enteringMessage, leavingMessage);
    }
}
