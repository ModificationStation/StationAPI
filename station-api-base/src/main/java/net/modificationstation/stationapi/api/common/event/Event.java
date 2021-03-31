package net.modificationstation.stationapi.api.common.event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Event {

    @Getter
    private final boolean cancellable = false;
    @Getter
    private boolean cancelled;

    public void setCancelled(boolean cancelled) {
        if (isCancellable())
            this.cancelled = cancelled;
        else
            throw new UnsupportedOperationException(String.format("Trying to cancel a not cancellable event! (%s)", getClass().getName()));
    }

    public final void cancel() {
        setCancelled(true);
    }

    public final void resume() {
        setCancelled(false);
    }

    protected abstract int getEventID();

    protected static final AtomicInteger NEXT_ID = new AtomicInteger();
}
