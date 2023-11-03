package net.modificationstation.stationapi.mixin.maths;

import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockPos.class)
public interface TilePosAccessor {
    @Mutable
    @Accessor("x")
    void stationapi_setX(int x);

    @Mutable
    @Accessor("y")
    void stationapi_setY(int y);

    @Mutable
    @Accessor("z")
    void stationapi_setZ(int z);
}
