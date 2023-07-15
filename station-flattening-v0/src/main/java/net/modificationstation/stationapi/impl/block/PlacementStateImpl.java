package net.modificationstation.stationapi.impl.block;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.maths.Vec3f;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.event.block.BlockEvent;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public final class PlacementStateImpl {

    @EventListener
    private static void handleBlockPlacement(BlockEvent.BeforePlacedByItem event) {
        BlockState placementState = event.block.getPlacementState(new ItemPlacementContext(
                event.player,
                event.blockItem,
                new HitResult(
                        event.x - event.side.getOffsetX(),
                        event.y - event.side.getOffsetY(),
                        event.z - event.side.getOffsetZ(),
                        event.side.getId(), Vec3f.from(event.x, event.y, event.z)
                )
        ));
        if (placementState != event.block.getDefaultState()) event.placeFunction = () -> event.world.setBlockStateWithMetadataWithNotify(event.x, event.y, event.z, placementState, event.meta) != null;
    }
}
