package net.modificationstation.stationapi.api.event.celestial;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.world.World;

@SuperBuilder
public class CelestialRegisterEvent extends Event {
    public final World world;
}
