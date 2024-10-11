package net.modificationstation.sltest.celestial;

import net.minecraft.world.World;
import net.modificationstation.stationapi.api.celestial.CelestialEvent;
import net.modificationstation.stationapi.api.util.Identifier;

public class DebugCelestialEvent extends CelestialEvent {
    public DebugCelestialEvent(int frequency, Identifier name) {
        super(frequency, name);
    }

    @Override
    public void onActivation(World world) {
        super.onActivation(world);
        System.out.println(this.getIdentifier() + " has begun");
    }

    @Override
    public void onDeactivation(World world) {
        super.onDeactivation(world);
        System.out.println(this.getIdentifier() + " is over");
    }
}
