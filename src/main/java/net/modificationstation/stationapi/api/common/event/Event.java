package net.modificationstation.stationapi.api.common.event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Event {

    public final boolean cancellable;
    @Getter
    private boolean cancelled;

    protected Event() {
        this(false);
    }

    public final void setCancelled(boolean cancelled) {
        if (cancellable)
            this.cancelled = cancelled;
        else
            throw new UnsupportedOperationException(String.format("Trying to cancel an uncancellable event! (%s)", getClass().getName()));
    }
}
