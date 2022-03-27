package net.modificationstation.stationapi.api.registry;

import net.minecraft.entity.player.PlayerBase;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.packet.Message;

import java.util.function.BiConsumer;

/**
 * Registry that holds {@link Message} listeners.
 *
 * <p>A message listener must have the same identifier as the message it listens for.
 *
 * @author mine_diver
 */
public final class MessageListenerRegistry extends Registry<BiConsumer<PlayerBase, Message>> {

    public static final MessageListenerRegistry INSTANCE = new MessageListenerRegistry(Identifier.of(StationAPI.MODID, "message_listeners"));

    private MessageListenerRegistry(Identifier identifier) {
        super(identifier);
    }
}
