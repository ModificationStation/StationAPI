package net.modificationstation.stationapi.api.server.event.network;

import lombok.RequiredArgsConstructor;
import net.minecraft.entity.player.ServerPlayer;
import net.modificationstation.stationapi.api.common.event.Event;
import net.modificationstation.stationapi.api.common.event.GameEventOld;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class PlayerLogin extends Event {

    public final ServerPlayer player;
}
