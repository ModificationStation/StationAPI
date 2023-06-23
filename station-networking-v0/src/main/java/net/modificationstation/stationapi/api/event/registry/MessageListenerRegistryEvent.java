package net.modificationstation.stationapi.api.event.registry;

import net.mine_diver.unsafeevents.event.EventPhases;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.registry.MessageListenerRegistry;

/**
 * Registry event that fires when {@link MessageListenerRegistry} is ready to register listeners.
 *
 * @author mine_diver
 */
@EventPhases(StationAPI.INTERNAL_PHASE)
public class MessageListenerRegistryEvent extends RegistryEvent<MessageListenerRegistry> {
    public MessageListenerRegistryEvent() {
        super(MessageListenerRegistry.INSTANCE);
    }
}
