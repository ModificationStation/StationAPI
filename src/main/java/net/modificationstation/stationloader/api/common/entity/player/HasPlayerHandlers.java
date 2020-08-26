package net.modificationstation.stationloader.api.common.entity.player;

import net.modificationstation.stationloader.api.common.entity.player.PlayerHandler;

import java.util.List;

public interface HasPlayerHandlers {

    List<PlayerHandler> getPlayerBases();
}