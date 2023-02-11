package net.modificationstation.stationapi.api.client.event.gui.screen;

import com.google.common.collect.ImmutableList;
import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.modificationstation.stationapi.api.client.gui.screen.EditWorldScreen;
import net.modificationstation.stationapi.api.client.gui.widget.ButtonWidgetDeferredDetachedContext;

@SuperBuilder
public abstract class EditWorldScreenEvent extends Event {

    @SuperBuilder
    public static class ScrollableButtonContextRegister extends EditWorldScreenEvent {

        public final ImmutableList.Builder<ButtonWidgetDeferredDetachedContext<EditWorldScreen>> contexts;

        @Override
        protected int getEventID() {
            return ID;
        }

        public static final int ID = NEXT_ID.incrementAndGet();
    }
}
