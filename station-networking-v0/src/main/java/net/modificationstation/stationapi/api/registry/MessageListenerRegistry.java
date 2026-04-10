package net.modificationstation.stationapi.api.registry;

import com.mojang.serialization.Lifecycle;
import net.minecraft.entity.player.PlayerEntity;
import net.modificationstation.stationapi.api.network.packet.MessagePacket;

import java.util.function.BiConsumer;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

/**
 * Registry that holds {@link MessagePacket} listeners.
 *
 * <p>A message listener must have the same identifier as the message it listens for.
 *
 * @author mine_diver
 */
public class MessageListenerRegistry extends SimpleRegistry<BiConsumer<PlayerEntity, MessagePacket>> {
    public static final RegistryKey<MessageListenerRegistry> KEY = RegistryKey.ofRegistry(NAMESPACE.id("message_listeners"));
    public static final MessageListenerRegistry INSTANCE = Registries.create(KEY, new MessageListenerRegistry(), Lifecycle.experimental());

    private MessageListenerRegistry() {
        super(KEY, Lifecycle.experimental());
    }
}
