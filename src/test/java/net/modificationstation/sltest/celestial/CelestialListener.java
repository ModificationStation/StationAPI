package net.modificationstation.sltest.celestial;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.sltest.SLTest;
import net.modificationstation.stationapi.api.celestial.CelestialEvent;
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
        flyingDimando = new CelestialEvent(2);
        fallingDimando = new CelestialEvent(4);
        flyingDimando.addIncompatibleEvent(fallingDimando);
    }
}
