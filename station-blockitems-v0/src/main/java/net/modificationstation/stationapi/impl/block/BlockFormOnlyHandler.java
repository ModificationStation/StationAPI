package net.modificationstation.stationapi.impl.block;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.Block;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.block.BlockItemFactoryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.util.Lazy;
import net.modificationstation.stationapi.api.util.UnsafeProvider;
import org.jetbrains.annotations.ApiStatus;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public class BlockFormOnlyHandler {

    @ApiStatus.Internal
    public static final Lazy<Block> EMPTY_BLOCK_ITEM = new Lazy<>(() -> {
        try {
            return (Block) UnsafeProvider.theUnsafe.allocateInstance(Block.class);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    });

    @EventListener
    private static void registerBlockItem(BlockItemFactoryEvent event) {
        if (event.block.isAutomaticBlockItemRegistrationDisabled()) event.cancel();
    }
}
