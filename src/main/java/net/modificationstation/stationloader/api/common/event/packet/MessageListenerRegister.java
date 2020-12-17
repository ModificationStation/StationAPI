package net.modificationstation.stationloader.api.common.event.packet;

import lombok.Getter;
import net.modificationstation.stationloader.api.common.event.ModEvent;
import net.modificationstation.stationloader.api.common.packet.MessageListenerRegistry;
import net.modificationstation.stationloader.api.common.registry.ModID;

public interface MessageListenerRegister {

    @SuppressWarnings("UnstableApiUsage")
    ModEvent<MessageListenerRegister> EVENT = new ModEvent<>(MessageListenerRegister.class,
            listeners ->
                    (registry, modID) -> {
                        for (MessageListenerRegister listener : listeners)
                            listener.registerMessageListeners(registry, MessageListenerRegister.EVENT.getListenerModID(listener));
                    },
            listener ->
                    (registry, modID) -> {
                        MessageListenerRegister.EVENT.setCurrentListener(listener);
                        listener.registerMessageListeners(registry, modID);
                        MessageListenerRegister.EVENT.setCurrentListener(null);
                    },
            messageListenerRegister ->
                    messageListenerRegister.register((registry, modID) -> ModEvent.post(new Data(registry)), null)
    );

    void registerMessageListeners(MessageListenerRegistry registry, ModID modID);

    final class Data extends ModEvent.Data<MessageListenerRegister> {

        @Getter
        private final MessageListenerRegistry registry;

        private Data(MessageListenerRegistry registry) {
            super(EVENT);
            this.registry = registry;
        }
    }
}
