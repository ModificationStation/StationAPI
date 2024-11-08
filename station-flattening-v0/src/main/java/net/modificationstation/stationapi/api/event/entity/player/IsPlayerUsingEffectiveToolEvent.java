package net.modificationstation.stationapi.api.event.entity.player;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.modificationstation.stationapi.api.block.BlockState;

import java.util.function.BooleanSupplier;

@SuperBuilder
public final class IsPlayerUsingEffectiveToolEvent extends Event {
    public final PlayerEntity player;
    public final BlockView blockView;
    public final BlockPos blockPos;
    public final BlockState blockState;
    public BooleanSupplier resultProvider;
}
