package net.modificationstation.stationapi.api.util.maths;

import net.minecraft.util.maths.TilePos;
import net.modificationstation.stationapi.mixin.block.TilePosAccessor;

public class MutableBlockPos extends TilePos {
    private final TilePosAccessor _super = (TilePosAccessor) this;

    public MutableBlockPos(int i, int j, int k) {
        super(i, j, k);
    }

    public MutableBlockPos set(int x, int y, int z) {
        _super.stationapi_setX(x);
        _super.stationapi_setY(y);
        _super.stationapi_setZ(z);
        return this;
    }
}
