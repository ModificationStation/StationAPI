package net.modificationstation.stationapi.api.event.registry;

import net.mine_diver.unsafeevents.event.EventPhases;
import net.minecraft.entity.player.PlayerEntity;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.network.packet.MessagePacket;
import net.modificationstation.stationapi.api.registry.MessageListenerRegistry;
import net.modificationstation.stationapi.api.registry.Registry;

import java.util.function.BiConsumer;

/**
 * Registry event that fires when {@link MessageListenerRegistry} is ready to register listeners.
 *
 * @author mine_diver
 */
@EventPhases(StationAPI.INTERNAL_PHASE)
public class MessageListenerRegistryEvent extends RegistryEvent.EntryTypeBound<BiConsumer<PlayerEntity, MessagePacket>, Registry<BiConsumer<PlayerEntity, MessagePacket>>> {
    public MessageListenerRegistryEvent() {
        super(MessageListenerRegistry.INSTANCE);
    }
}
