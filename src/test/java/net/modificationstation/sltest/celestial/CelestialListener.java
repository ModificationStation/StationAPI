package net.modificationstation.sltest.celestial;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.sltest.SLTest;
import net.modificationstation.stationapi.api.celestial.CelestialEvent;
import net.modificationstation.stationapi.api.celestial.CelestialTimeManager;
import net.modificationstation.stationapi.api.event.celestial.CelestialRegisterEvent;

public class CelestialListener {

    private boolean hasRegistered = false; // Workaround to prevent excessive registering

    public static CelestialEvent flyingDimando;
    public static CelestialEvent fallingDimando;

    @EventListener
    public void registerCelestialEvents(CelestialRegisterEvent event) {
        if (hasRegistered) return;
        hasRegistered = true;
        SLTest.LOGGER.info("Register celestial events for testing");
        flyingDimando = new CelestialEvent(2, "Flying Dimando");
        fallingDimando = new CelestialEvent(4, "Falling Dimando");
        flyingDimando.addIncompatibleEvent(fallingDimando);
        CelestialTimeManager.addMorningEvent(flyingDimando);
        CelestialTimeManager.addNoonEvent(fallingDimando);
    }
}
