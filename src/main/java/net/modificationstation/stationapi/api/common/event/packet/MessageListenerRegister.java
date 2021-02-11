package net.modificationstation.stationapi.api.common.event.packet;

import net.modificationstation.stationapi.api.common.event.ModEventOld;
import net.modificationstation.stationapi.api.common.packet.MessageListenerRegistry;
import net.modificationstation.stationapi.api.common.registry.ModID;

public interface MessageListenerRegister {

    ModEventOld<MessageListenerRegister> EVENT = new ModEventOld<>(MessageListenerRegister.class,
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
                    messageListenerRegister.register((registry, modID) -> ModEventOld.post(new Data(registry)), null)
    );

    void registerMessageListeners(MessageListenerRegistry registry, ModID modID);

    final class Data extends ModEventOld.Data<MessageListenerRegister> {

        public final MessageListenerRegistry registry;

        private Data(MessageListenerRegistry registry) {
            super(EVENT);
            this.registry = registry;
        }
    }
}
