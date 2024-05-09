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
    public static CelestialEvent spinningDimando;
    public static CelestialEvent burningDimando;

    @EventListener
    public void registerCelestialEvents(CelestialRegisterEvent event) {
        if (hasRegistered) return;
        hasRegistered = true;
        SLTest.LOGGER.info("Register celestial events for testing");
        flyingDimando = new CelestialEvent(4, "Flying Dimando");
        fallingDimando = new CelestialEvent(2, "Falling Dimando");
        spinningDimando = new CelestialEvent(4, "Spinning Dimando").setDayOffset(1);
        burningDimando = new CelestialEvent(2, "Burning Dimando").setDayOffset(1);
        flyingDimando.addIncompatibleEvent(fallingDimando);
        CelestialTimeManager.addMorningEvent(flyingDimando);
        CelestialTimeManager.addNoonEvent(fallingDimando);
        CelestialTimeManager.addEveningEvent(spinningDimando);
        CelestialTimeManager.addMidnightEvent(burningDimando);
    }
}
