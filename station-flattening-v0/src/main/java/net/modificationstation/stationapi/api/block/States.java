package net.modificationstation.stationapi.api.block;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.util.Lazy;
import net.modificationstation.stationapi.api.util.collection.IdList;

import static net.modificationstation.stationapi.api.registry.Identifier.of;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public final class States {

    private static final Lazy<BlockBase> AIR_BLOCK = new Lazy<>(() -> new Air(0));

    public static final Lazy<BlockState> AIR = new Lazy<>(() -> ((BlockStateHolder) AIR_BLOCK.get()).getDefaultState());

    public static final IdList<BlockState> STATE_IDS = new IdList<>();

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4)
    private static void registerBlocks(BlockRegistryEvent event) {
        Registry.register(event.registry, of("air"), AIR_BLOCK.get());
    }

    @EventListener(numPriority = Integer.MIN_VALUE / 2 + Integer.MIN_VALUE / 4)
    private static void registerStates(BlockRegistryEvent event) {
        BlockRegistry.INSTANCE.forEach(blockBase -> ((BlockStateHolder) blockBase).getStateManager().getStates().forEach(STATE_IDS::add));
    }
}
