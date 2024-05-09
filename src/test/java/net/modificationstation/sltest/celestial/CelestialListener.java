package net.modificationstation.sltest.celestial;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.sltest.SLTest;
import net.modificationstation.stationapi.api.celestial.CelestialEvent;
import net.modificationstation.stationapi.api.celestial.CelestialTimeManager;
import net.modificationstation.stationapi.api.celestial.DayQuarter;
import net.modificationstation.stationapi.api.event.celestial.CelestialRegisterEvent;

public class CelestialListener {

    private boolean hasRegistered = false; // Workaround to prevent excessive registering

    public static CelestialEvent flyingDimando;
    public static CelestialEvent fallingDimando;
    public static CelestialEvent spinningDimando;
    public static CelestialEvent burningDimando;
    public static CelestialEvent longDimando;

    @EventListener
    public void registerCelestialEvents(CelestialRegisterEvent event) {
        if (hasRegistered) return;
        hasRegistered = true;
        SLTest.LOGGER.info("Register celestial events for testing");
        flyingDimando = new CelestialEvent(4, "Flying Dimando");
        fallingDimando = new CelestialEvent(2, "Falling Dimando");
        spinningDimando = new CelestialEvent(4, "Spinning Dimando").setDayOffset(1);
        burningDimando = new CelestialEvent(2, "Burning Dimando").setDayOffset(1);
        longDimando = new CelestialEvent(12, "Long Dimando").setExtraDays(4);
        flyingDimando.addIncompatibleEvent(fallingDimando);
        spinningDimando.addIncompatibleEvent(burningDimando);
        CelestialTimeManager.addCelestialEvent(flyingDimando, DayQuarter.MORNING, DayQuarter.MORNING);
        CelestialTimeManager.addCelestialEvent(fallingDimando, DayQuarter.NOON, DayQuarter.MIDNIGHT);
        CelestialTimeManager.addCelestialEvent(spinningDimando, DayQuarter.EVENING, DayQuarter.NOON);
        CelestialTimeManager.addCelestialEvent(burningDimando, DayQuarter.MIDNIGHT, DayQuarter.EVENING);
        CelestialTimeManager.addCelestialEvent(longDimando, DayQuarter.MIDNIGHT, DayQuarter.MIDNIGHT);
    }
}
