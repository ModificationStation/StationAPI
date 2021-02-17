package net.modificationstation.stationapi.api.common.event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event {

    @Getter
    private final boolean cancellable = false;
    @Getter
    private boolean cancelled;

    public void setCancelled(boolean cancelled) {
        if (isCancellable())
            this.cancelled = cancelled;
        else
            throw new UnsupportedOperationException(String.format("Trying to cancel an uncancellable event! (%s)", getClass().getName()));
    }

    public final void cancel() {
        setCancelled(true);
    }

    public final void resume() {
        setCancelled(false);
    }
}
