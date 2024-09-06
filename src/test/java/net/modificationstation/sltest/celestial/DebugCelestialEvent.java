package net.modificationstation.sltest.celestial;

import net.minecraft.world.World;
import net.modificationstation.stationapi.api.celestial.CelestialEvent;

public class DebugCelestialEvent extends CelestialEvent {
    public DebugCelestialEvent(int frequency, String name, World world) {
        super(frequency, name, world);
    }

    @Override
    public void onActivation() {
        super.onActivation();
        System.out.println(this.getName() + " has begun");
    }

    @Override
    public void onDeactivation() {
        super.onDeactivation();
        System.out.println(this.getName() + " is over");
    }
}
