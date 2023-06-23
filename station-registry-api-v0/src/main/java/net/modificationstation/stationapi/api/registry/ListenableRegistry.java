package net.modificationstation.stationapi.api.registry;

import net.mine_diver.unsafeevents.MutableEventBus;

public interface ListenableRegistry {
	MutableEventBus getEventBus();
}
