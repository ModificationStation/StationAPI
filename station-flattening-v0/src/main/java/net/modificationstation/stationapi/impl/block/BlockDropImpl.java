package net.modificationstation.stationapi.impl.block;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.DropListProvider;
import net.modificationstation.stationapi.api.event.block.BlockEvent;
import net.modificationstation.stationapi.api.level.BlockStateView;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.mixin.flattening.BlockBaseAccessor;

import java.util.List;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class BlockDropImpl {

    private static boolean cached;
    private static Level cachedLevel;
    private static int cachedX, cachedY, cachedZ;
    private static BlockState cachedState;

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4)
    private static void cacheBlockBeingBroken(BlockEvent.BeforeRemoved event) {
        cached = true;
        cachedLevel = event.level;
        cachedX = event.x;
        cachedY = event.y;
        cachedZ = event.z;
        cachedState = ((BlockStateView) event.level).getBlockState(event.x, event.y, event.z);
    }

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4)
    private static void handleDrop(BlockEvent.BeforeDrop event) {
        if (cached && event.level == cachedLevel && event.x == cachedX && event.y == cachedY && event.z == cachedZ && event.block == cachedState.getBlock()) {
            List<ItemInstance> drops = ((DropListProvider) event.block).getDropList(event.level, event.x, event.y, event.z, event.meta, cachedState);
            if (drops != null) {
                drops.forEach(drop -> {
                    if (!(event.level.rand.nextFloat() > event.chance) && drop.itemId != 0)
                        ((BlockBaseAccessor) event.block).invokeDrop(event.level, event.x, event.y, event.z, drop);
                });
                cached = false;
                cachedLevel = null;
                cachedX = cachedY = cachedZ = 0;
                cachedState = null;
                event.cancel();
            }
        }
    }
}
