package net.modificationstation.stationapi.api.server.event.network;

import lombok.RequiredArgsConstructor;
import net.minecraft.entity.player.ServerPlayer;
import net.modificationstation.stationapi.api.common.event.Event;

@RequiredArgsConstructor
public class PlayerLoginEvent extends Event {

    public final ServerPlayer player;
}
