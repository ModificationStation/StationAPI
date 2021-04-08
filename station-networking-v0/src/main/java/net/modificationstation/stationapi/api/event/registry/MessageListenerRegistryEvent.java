package net.modificationstation.stationapi.api.event.registry;

import net.modificationstation.stationapi.api.registry.MessageListenerRegistry;

public class MessageListenerRegistryEvent extends RegistryEvent<MessageListenerRegistry> {

    public MessageListenerRegistryEvent() {
        super(MessageListenerRegistry.INSTANCE);
    }

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
