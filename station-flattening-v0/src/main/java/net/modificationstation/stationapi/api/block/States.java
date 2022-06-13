package net.modificationstation.stationapi.api.block;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.util.Lazy;
import net.modificationstation.stationapi.api.util.collection.IdList;
import net.modificationstation.stationapi.impl.block.BlockRegistryInit;

public class States {

    public static final Lazy<BlockState> AIR = new Lazy<>(() -> ((BlockStateHolder) BlockRegistryInit.AIR.get()).getDefaultState());

    public static final IdList<BlockState> STATE_IDS = new IdList<>();

    @EventListener(priority = ListenerPriority.LOW)
    private static void registerStates(BlockRegistryEvent event) {
        BlockRegistry.INSTANCE.forEach((identifier, blockBase) -> ((BlockStateHolder) blockBase).getStateManager().getStates().forEach(STATE_IDS::add));
    }
}
