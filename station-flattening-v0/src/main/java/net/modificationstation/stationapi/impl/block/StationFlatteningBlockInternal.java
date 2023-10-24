package net.modificationstation.stationapi.impl.block;

import net.modificationstation.stationapi.api.block.BlockState;

import java.util.function.ToIntFunction;

public interface StationFlatteningBlockInternal {
    ToIntFunction<BlockState> stationapi_getLuminanceProvider();
}
