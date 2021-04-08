package net.modificationstation.stationapi.api.registry;

import net.minecraft.entity.player.PlayerBase;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.packet.Message;

import java.util.function.*;

public final class MessageListenerRegistry extends Registry<BiConsumer<PlayerBase, Message>> {

    public static final MessageListenerRegistry INSTANCE = new MessageListenerRegistry(Identifier.of(StationAPI.MODID, "message_listeners"));

    private MessageListenerRegistry(Identifier identifier) {
        super(identifier);
    }

    @Override
    public int getRegistrySize() {
        return Integer.MAX_VALUE;
    }
}
