package net.modificationstation.sltest.celestial;

import net.minecraft.world.World;

public class FlyingDimando extends DebugCelestialEvent {
    public FlyingDimando(int frequency, String name, World world) {
        super(frequency, name, world);
    }

    @Override
    public void onActivation() {
        super.onActivation();
        world.method_262().setRaining(true);
        world.method_262().setRainTime(200);
    }
}
