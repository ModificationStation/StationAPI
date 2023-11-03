package net.modificationstation.stationapi.api.event.level;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.world.World;

@SuperBuilder
public abstract class LevelEvent extends Event {
    public final World level;

    @SuperBuilder
    public static class Init extends LevelEvent {}
}
