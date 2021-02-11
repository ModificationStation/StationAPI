package net.modificationstation.stationapi.api.common.event;

public final class DeadEvent extends Event {

    public final Event event;

    DeadEvent(Event event) {
        super(false);
        this.event = event;
    }
}
