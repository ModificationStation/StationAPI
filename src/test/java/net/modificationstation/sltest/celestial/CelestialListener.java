package net.modificationstation.sltest.celestial;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.sltest.MainTest;
import net.modificationstation.sltest.SLTest;
import net.modificationstation.stationapi.api.celestial.CelestialActivityStateManager;
import net.modificationstation.stationapi.api.celestial.CelestialEvent;
import net.modificationstation.stationapi.api.celestial.CelestialTimeManager;
import net.modificationstation.stationapi.api.celestial.DayQuarter;
import net.modificationstation.stationapi.api.event.celestial.CelestialRegisterEvent;
import net.modificationstation.stationapi.api.util.Identifier;

public class CelestialListener {

    public static CelestialEvent flyingDimando;
    public static CelestialEvent fallingDimando;
    public static CelestialEvent crashingDimando;
    public static CelestialEvent spinningDimando;
    public static CelestialEvent burningDimando;
    public static CelestialEvent longDimando;

    @EventListener
    public void registerCelestialEvents(CelestialRegisterEvent event) {
        SLTest.LOGGER.info("Register celestial events for testing");
        flyingDimando = new FlyingDimando(4, Identifier.of(SLTest.NAMESPACE, "flying_dimando"));
        fallingDimando = new DebugCelestialEvent(2, Identifier.of(SLTest.NAMESPACE, "falling_dimando"));
        crashingDimando = new DebugCelestialEvent(2, Identifier.of(SLTest.NAMESPACE, "crashing_dimando"));
        spinningDimando = new DebugCelestialEvent(4, Identifier.of(SLTest.NAMESPACE, "spinning_dimando")).setDayOffset(event.world, 1);
        burningDimando = new DebugCelestialEvent(2, Identifier.of(SLTest.NAMESPACE, "burning_dimando")).setDayOffset(event.world, 1);
        longDimando = new DebugCelestialEvent(12, Identifier.of(SLTest.NAMESPACE, "long_dimando")).setExtraDays(event.world, 4);
        flyingDimando.addIncompatibleEvent(fallingDimando);
        spinningDimando.addIncompatibleEvent(burningDimando);
        crashingDimando.addDependency(longDimando);
        ((CelestialActivityStateManager) event.world).getCelestialTimeManager().addCelestialEvent(flyingDimando, DayQuarter.MORNING, DayQuarter.MORNING);
        ((CelestialActivityStateManager) event.world).getCelestialTimeManager().addCelestialEvent(fallingDimando, DayQuarter.NOON, DayQuarter.MIDNIGHT);
        ((CelestialActivityStateManager) event.world).getCelestialTimeManager().addCelestialEvent(crashingDimando, DayQuarter.NOON, DayQuarter.NOON);
        ((CelestialActivityStateManager) event.world).getCelestialTimeManager().addCelestialEvent(spinningDimando, DayQuarter.EVENING, DayQuarter.NOON);
        ((CelestialActivityStateManager) event.world).getCelestialTimeManager().addCelestialEvent(burningDimando, DayQuarter.MIDNIGHT, DayQuarter.EVENING);
        ((CelestialActivityStateManager) event.world).getCelestialTimeManager().addCelestialEvent(longDimando, DayQuarter.MIDNIGHT, DayQuarter.MIDNIGHT);
    }
}
