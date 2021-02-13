package net.modificationstation.stationapi.api.common.event.entity.player;

import lombok.RequiredArgsConstructor;
import net.minecraft.entity.player.PlayerBase;
import net.modificationstation.stationapi.api.common.event.Event;

@RequiredArgsConstructor
public class PlayerEvent extends Event {

    public final PlayerBase player;
}
