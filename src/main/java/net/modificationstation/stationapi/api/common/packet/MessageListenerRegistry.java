package net.modificationstation.stationapi.api.common.packet;

import net.minecraft.entity.player.PlayerBase;
import net.modificationstation.stationapi.api.common.registry.Identifier;
import net.modificationstation.stationapi.api.common.registry.Registry;
import net.modificationstation.stationapi.impl.common.StationAPI;

import java.util.function.BiConsumer;

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
