package net.modificationstation.stationloader.api.common.event.packet;

import net.modificationstation.stationloader.api.common.event.ModEvent;
import net.modificationstation.stationloader.api.common.packet.MessageListenerRegistry;
import net.modificationstation.stationloader.api.common.registry.ModID;

public interface MessageListenerRegister {

    ModEvent<MessageListenerRegister> EVENT = new ModEvent<>(MessageListenerRegister.class, listeners ->
            (messageListeners, modID) -> {
                for (MessageListenerRegister listener : listeners)
                    listener.registerMessageListeners(messageListeners, MessageListenerRegister.EVENT.getListenerModID(listener));
            });

    void registerMessageListeners(MessageListenerRegistry messageListeners, ModID modID);
}
