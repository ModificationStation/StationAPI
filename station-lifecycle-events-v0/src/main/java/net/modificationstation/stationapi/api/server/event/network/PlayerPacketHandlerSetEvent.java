package net.modificationstation.stationapi.api.server.event.network;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.entity.player.ServerPlayer;

@SuperBuilder
public class PlayerPacketHandlerSetEvent extends Event {

    public final ServerPlayer player;

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
