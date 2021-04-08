package net.modificationstation.stationapi.api.event.registry;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.mine_diver.unsafeevents.Event;
import net.modificationstation.stationapi.api.registry.Registry;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class RegistryEvent<T extends Registry<?>> extends Event {

    public final T registry;

//    public static class GuiHandlers extends RegistryEvent<GuiHandlerRegistry> {
//
//        public GuiHandlers() {
//            super(GuiHandlerRegistry.INSTANCE);
//        }
//
//        @Override
//        protected int getEventID() {
//            return ID;
//        }
//
//        public static final int ID = NEXT_ID.incrementAndGet();
//    }
//
//    public static class MessageListeners extends RegistryEvent<MessageListenerRegistry> {
//
//        public MessageListeners() {
//            super(MessageListenerRegistry.INSTANCE);
//        }
//
//        @Override
//        protected int getEventID() {
//            return ID;
//        }
//
//        public static final int ID = NEXT_ID.incrementAndGet();
//    }
}
