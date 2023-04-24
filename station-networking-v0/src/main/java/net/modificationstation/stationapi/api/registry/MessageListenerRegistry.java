package net.modificationstation.stationapi.api.registry;

import com.mojang.serialization.Lifecycle;
import net.minecraft.entity.player.PlayerBase;
import net.modificationstation.stationapi.api.packet.Message;

import java.util.function.BiConsumer;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

/**
 * Registry that holds {@link Message} listeners.
 *
 * <p>A message listener must have the same identifier as the message it listens for.
 *
 * @author mine_diver
 */
public final class MessageListenerRegistry extends SimpleRegistry<BiConsumer<PlayerBase, Message>> {

    private static final BiConsumer<PlayerBase, Message> EMPTY = (player, message) -> {};
    public static final RegistryKey<MessageListenerRegistry> KEY = RegistryKey.ofRegistry(MODID.id("message_listeners"));
    public static final MessageListenerRegistry INSTANCE = Registries.create(KEY, new MessageListenerRegistry(), registry -> EMPTY, Lifecycle.experimental());

    private MessageListenerRegistry() {
        super(KEY, Lifecycle.experimental(), false);
    }
}
