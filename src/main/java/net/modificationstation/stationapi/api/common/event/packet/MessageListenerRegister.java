package net.modificationstation.stationapi.api.common.event.packet;

import lombok.RequiredArgsConstructor;
import net.modificationstation.stationapi.api.common.event.Event;
import net.modificationstation.stationapi.api.common.packet.MessageListenerRegistry;

@RequiredArgsConstructor
public class MessageListenerRegister extends Event {

    public final MessageListenerRegistry registry;
}
