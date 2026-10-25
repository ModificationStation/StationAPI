package net.modificationstation.stationapi.api.client.gui.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.modificationstation.stationapi.api.util.Util;

public interface StationScreenHandler {
    default void stationAPI$onClose(PlayerEntity playerEntity) {
        Util.assertImpl();
    }
}
