package net.modificationstation.stationapi.api.lookup.block;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.lookup.EventBasedAPILookup;
import net.modificationstation.stationapi.api.util.API;

import java.util.Optional;
import java.util.function.BiFunction;

public final class BlockAPILookup {

    private final EventBasedAPILookup<BlockEntity> BLOCK_ENTITY_API_LOOKUP = new EventBasedAPILookup<>(BlockEntityAPILookupEvent::new);
    private final EventBasedAPILookup<Block> BLOCK_API_LOOKUP = new EventBasedAPILookup<>(BlockAPILookupEvent::new);

    @API
    public <T> Optional<T> find(Class<T> api, World world, BlockPos pos) {
        var be = world.method_1777(pos.x, pos.y, pos.z);
        if (be != null) {
            var beApi = BLOCK_ENTITY_API_LOOKUP.find(api, be);
            if (beApi.isPresent()) {
                return beApi;
            }
        }
        return BLOCK_API_LOOKUP.find(api, world.getBlockState(pos).getBlock());
    }

    @API
    public <T> BiFunction<World, BlockPos, Optional<T>> finder(Class<T> api) {
        return (world, blockPos) -> this.find(api, world, blockPos);
    }
}
