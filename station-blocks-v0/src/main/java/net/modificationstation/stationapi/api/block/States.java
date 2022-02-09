package net.modificationstation.stationapi.api.block;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.util.Lazy;
import net.modificationstation.stationapi.api.util.collection.IdList;

import static net.modificationstation.stationapi.api.block.Properties.META;

public class States {

    public static final Lazy<BlockState> AIR = new Lazy<>(() -> {
        StateManager.Builder<BlockBase, BlockState> builder = new StateManager.Builder<>(null);
        builder.add(META);
        StateManager<BlockBase, BlockState> stateManager = builder.build(blockBase -> ((BlockStateHolder) blockBase).getDefaultState(), BlockState::new);
        return stateManager.getDefaultState().with(META, 0);
    });

    public static final IdList<BlockState> STATE_IDS = new IdList<>();

    @EventListener(priority = ListenerPriority.LOW)
    private static void registerStates(BlockRegistryEvent event) {
        BlockRegistry.INSTANCE.forEach((identifier, blockBase) -> {
            if (blockBase == null)
                STATE_IDS.add(AIR.get());
            else
                ((BlockStateHolder) blockBase).getStateManager().getStates().forEach(STATE_IDS::add);
        });
    }
}
