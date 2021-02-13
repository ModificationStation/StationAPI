package net.modificationstation.stationapi.api.common.event.entity.player;

import net.minecraft.entity.player.PlayerBase;
import net.modificationstation.stationapi.api.common.entity.player.PlayerHandler;

import java.util.List;

public class PlayerHandlerRegister extends PlayerEvent {

    public final List<PlayerHandler> playerHandlers;

    public PlayerHandlerRegister(PlayerBase player, List<PlayerHandler> playerHandlers) {
        super(player);
        this.playerHandlers = playerHandlers;
    }
}
