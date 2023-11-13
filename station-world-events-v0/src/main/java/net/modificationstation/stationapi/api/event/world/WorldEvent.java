package net.modificationstation.stationapi.api.event.world;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.world.World;

@SuperBuilder
public abstract class WorldEvent extends Event {
    public final World level;

    @SuperBuilder
    public static class Init extends WorldEvent {}
}
