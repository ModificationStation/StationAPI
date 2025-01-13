package net.modificationstation.stationapi.impl.block;

import com.google.common.base.Suppliers;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.Listener;
import net.minecraft.item.BlockItem;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.block.BlockItemFactoryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.util.UnsafeProvider;
import org.jetbrains.annotations.ApiStatus;

import java.lang.invoke.MethodHandles;
import java.util.function.Supplier;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public class BlockFormOnlyHandler {
    static {
        Listener.registerLookup(MethodHandles.lookup());
    }

    @ApiStatus.Internal
    public static final Supplier<BlockItem> EMPTY_BLOCK_ITEM = Suppliers.memoize(() -> {
        try {
            return (BlockItem) UnsafeProvider.theUnsafe.allocateInstance(BlockItem.class);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    });

    @EventListener
    private static void registerBlockItem(BlockItemFactoryEvent event) {
        if (event.block.isAutoItemRegistrationDisabled()) event.cancel();
    }
}
