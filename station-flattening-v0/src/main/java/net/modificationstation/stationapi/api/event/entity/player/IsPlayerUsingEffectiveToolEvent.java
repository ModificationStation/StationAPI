package net.modificationstation.stationapi.api.event.entity.player;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.entity.player.PlayerEntity;
import net.modificationstation.stationapi.api.block.BlockState;

import java.util.function.BooleanSupplier;

@SuperBuilder
public final class IsPlayerUsingEffectiveToolEvent extends Event {
    public final PlayerEntity player;
    public final BlockState blockState;
    public BooleanSupplier resultProvider;
}
