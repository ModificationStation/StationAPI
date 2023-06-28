package net.modificationstation.stationapi.api.event.registry;

import net.mine_diver.unsafeevents.event.EventPhases;
import net.modificationstation.stationapi.api.StationAPI;
import net.minecraft.entity.player.PlayerBase;
import net.modificationstation.stationapi.api.packet.Message;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.MessageListenerRegistry;
import net.modificationstation.stationapi.api.registry.Registry;

import java.util.function.BiConsumer;

/**
 * Registry event that fires when {@link MessageListenerRegistry} is ready to register listeners.
 *
 * @deprecated Use {@link Registry#register(Registry, Identifier, Object)} with {@code main} entrypoint instead
 *
 * @author mine_diver
 */
@Deprecated
@EventPhases(StationAPI.INTERNAL_PHASE)
public class MessageListenerRegistryEvent extends RegistryEvent<Registry<BiConsumer<PlayerBase, Message>>> {
    public MessageListenerRegistryEvent() {
        super(MessageListenerRegistry.INSTANCE);
    }
}
