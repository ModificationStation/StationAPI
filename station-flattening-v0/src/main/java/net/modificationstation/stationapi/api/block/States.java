package net.modificationstation.stationapi.api.block;

import com.google.common.base.Suppliers;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.util.collection.IdList;

import java.util.function.Supplier;

import static net.modificationstation.stationapi.api.util.Identifier.of;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public final class States {

    private static final Supplier<Block> AIR_BLOCK = Suppliers.memoize(() -> new Air(0));

    public static final Supplier<BlockState> AIR = Suppliers.memoize(() -> AIR_BLOCK.get().getDefaultState());

    /**
     * @deprecated Use {@link Block#STATE_IDS} instead
     */
    @Deprecated
    public static final IdList<BlockState> STATE_IDS = Block.STATE_IDS;

    @EventListener
    private static void registerBlocks(BlockRegistryEvent event) {
        Registry.register(event.registry, of("air"), AIR_BLOCK.get());
    }
}
