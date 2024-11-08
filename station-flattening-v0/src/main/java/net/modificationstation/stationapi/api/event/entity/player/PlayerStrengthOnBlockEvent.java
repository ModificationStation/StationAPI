package net.modificationstation.stationapi.api.event.entity.player;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.entity.player.PlayerEntity;
import net.modificationstation.stationapi.api.block.BlockState;

@SuperBuilder
public final class PlayerStrengthOnBlockEvent extends Event {
    public interface ResultProvider { float getAsFloat(); }

    public final PlayerEntity player;
    public final BlockState blockState;
    public ResultProvider resultProvider;
}
