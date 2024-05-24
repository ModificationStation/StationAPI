package net.modificationstation.sltest.celestial;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.sltest.SLTest;
import net.modificationstation.stationapi.api.celestial.CelestialEvent;
import net.modificationstation.stationapi.api.celestial.CelestialTimeManager;
import net.modificationstation.stationapi.api.celestial.DayQuarter;
import net.modificationstation.stationapi.api.event.celestial.CelestialRegisterEvent;

public class CelestialListener {

    public static CelestialEvent flyingDimando;
    public static CelestialEvent fallingDimando;
    public static CelestialEvent spinningDimando;
    public static CelestialEvent burningDimando;
    public static CelestialEvent longDimando;

    @EventListener
    public void registerCelestialEvents(CelestialRegisterEvent event) {
        SLTest.LOGGER.info("Register celestial events for testing");
        flyingDimando = new FlyingDimando(4, "flying_dimando", event.world);
        fallingDimando = new DebugCelestialEvent(2, "falling_dimando", event.world);
        spinningDimando = new DebugCelestialEvent(4, "spinning_dimando", event.world).setDayOffset(1);
        burningDimando = new DebugCelestialEvent(2, "burning_dimando", event.world).setDayOffset(1);
        longDimando = new DebugCelestialEvent(12, "long_dimando", event.world).setExtraDays(4);
        flyingDimando.addIncompatibleEvent(fallingDimando);
        spinningDimando.addIncompatibleEvent(burningDimando);
        CelestialTimeManager.addCelestialEvent(flyingDimando, DayQuarter.MORNING, DayQuarter.MORNING);
        CelestialTimeManager.addCelestialEvent(fallingDimando, DayQuarter.NOON, DayQuarter.MIDNIGHT);
        CelestialTimeManager.addCelestialEvent(spinningDimando, DayQuarter.EVENING, DayQuarter.NOON);
        CelestialTimeManager.addCelestialEvent(burningDimando, DayQuarter.MIDNIGHT, DayQuarter.EVENING);
        CelestialTimeManager.addCelestialEvent(longDimando, DayQuarter.MIDNIGHT, DayQuarter.MIDNIGHT);
    }
}
