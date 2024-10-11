package net.modificationstation.sltest.celestial;

import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;

public class FlyingDimando extends DebugCelestialEvent {
    public FlyingDimando(int frequency, Identifier name) {
        super(frequency, name);
    }

    @Override
    public void onActivation(World world) {
        super.onActivation(world);
        world.method_262().setRaining(true);
        world.method_262().setRainTime(200);
    }
}
