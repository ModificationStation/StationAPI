package net.modificationstation.stationapi.api.lookup.block;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.lookup.ApiLookup;
import net.modificationstation.stationapi.api.util.API;

import java.util.Optional;
import java.util.function.BiFunction;

public final class BlockAPILookup {

    @API
    public <T> Optional<T> find(Class<T> api, World world, BlockPos pos) {
        var be = world.method_1777(pos.x, pos.y, pos.z);
        if (be != null) {
            var beApi = ApiLookup.fromEvent(api, new BlockEntityAPILookupEvent(be, api));
            if (beApi.isPresent()) {
                return beApi;
            }
        }
        return ApiLookup.fromEvent(api, new BlockAPILookupEvent(api, world, world.getBlockState(pos)));
    }

    @API
    public <T> BiFunction<World, BlockPos, Optional<T>> finder(Class<T> api) {
        return (world, blockPos) -> this.find(api, world, blockPos);
    }
}
