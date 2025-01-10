package net.modificationstation.stationapi.impl.block;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.Listener;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.event.block.BlockEvent;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;

import java.lang.invoke.MethodHandles;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public final class PlacementStateImpl {
    static {
        Listener.registerLookup(MethodHandles.lookup());
    }

    @EventListener
    private static void handleBlockPlacement(BlockEvent.BeforePlacedByItem event) {
        BlockState placementState = event.block.getPlacementState(new ItemPlacementContext(
                event.player,
                event.blockItem,
                new HitResult(
                        event.x - event.side.getOffsetX(),
                        event.y - event.side.getOffsetY(),
                        event.z - event.side.getOffsetZ(),
                        event.side.getId(), Vec3d.createCached(event.x, event.y, event.z)
                )
        ));
        if (placementState != event.block.getDefaultState()) event.placeFunction = () -> event.world.setBlockStateWithMetadataWithNotify(event.x, event.y, event.z, placementState, event.meta) != null;
    }
}
