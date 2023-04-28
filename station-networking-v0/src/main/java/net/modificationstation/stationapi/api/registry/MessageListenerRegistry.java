package net.modificationstation.stationapi.api.registry;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessageListenerRegistry {

    private static final BiConsumer<PlayerBase, Message> EMPTY = (player, message) -> {};
    public static final RegistryKey<Registry<BiConsumer<PlayerBase, Message>>> KEY = RegistryKey.ofRegistry(MODID.id("message_listeners"));
    public static final Registry<BiConsumer<PlayerBase, Message>> INSTANCE = Registries.create(KEY, registry -> EMPTY);
}
