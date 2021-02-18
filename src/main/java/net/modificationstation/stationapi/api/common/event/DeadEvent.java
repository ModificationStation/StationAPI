package net.modificationstation.stationapi.api.common.event;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public final class DeadEvent extends Event {

    public final Event event;
}
