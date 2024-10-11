package net.modificationstation.stationapi.api.celestial;

public interface CelestialActivityStateManager {

    CelestialEventActivityState getCelestialEvents();
    CelestialTimeManager getCelestialTimeManager();
}
