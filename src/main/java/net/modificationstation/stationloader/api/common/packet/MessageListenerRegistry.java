package net.modificationstation.stationloader.api.common.packet;

import net.minecraft.entity.player.PlayerBase;
import net.modificationstation.stationloader.api.common.StationLoader;
import net.modificationstation.stationloader.api.common.registry.Identifier;
import net.modificationstation.stationloader.api.common.registry.Registry;

import java.util.function.BiConsumer;

public final class MessageListenerRegistry extends Registry<BiConsumer<PlayerBase, Message>> {

    private MessageListenerRegistry(Identifier identifier) {
        super(identifier);
    }

    @Override
    public int getRegistrySize() {
        return Integer.MAX_VALUE;
    }

    public static final MessageListenerRegistry INSTANCE = new MessageListenerRegistry(Identifier.of(StationLoader.INSTANCE.getModID(), "message_listeners"));
}
