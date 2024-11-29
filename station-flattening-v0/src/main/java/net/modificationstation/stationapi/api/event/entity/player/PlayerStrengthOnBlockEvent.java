package net.modificationstation.stationapi.api.event.entity.player;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.modificationstation.stationapi.api.block.BlockState;

@SuperBuilder
public final class PlayerStrengthOnBlockEvent extends Event {
    public interface ResultProvider { float getAsFloat(); }

    public final PlayerEntity player;
    public final BlockView blockView;
    public final BlockPos blockPos;
    public final BlockState blockState;
    /**
     * The function that determines the player's strength on the block.
     *
     * <p>
     *     Initially, the field contains a reimplementation of the vanilla
     *     {@link net.minecraft.entity.player.PlayerInventory#method_674(Block)}.
     * </p>
     * <p>
     *     The field is non-final to allow full control over the outcome of the event, including completely
     *     canceling the original chain of execution.
     * </p>
     * <p>
     *     The results can be chained by preserving the previous <code>resultProvider</code> instance
     *     and using it in your own implementation, for example:
     *     <pre>{@code
     *         var previousProvider = event.resultProvider;
     *         event.resultProvider = () -> doSomethingElse() && previousProvider.getAsBoolean()
     *     }</pre>
     * </p>
     */
    public ResultProvider resultProvider;
}
