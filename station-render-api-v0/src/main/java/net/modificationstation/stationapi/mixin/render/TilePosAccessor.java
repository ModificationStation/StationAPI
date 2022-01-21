package net.modificationstation.stationapi.mixin.render;

import net.minecraft.util.maths.TilePos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TilePos.class)
public interface TilePosAccessor {

    @Mutable
    @Accessor("x")
    void stationapi$setX(int x);

    @Mutable
    @Accessor("y")
    void stationapi$setY(int y);

    @Mutable
    @Accessor("z")
    void stationapi$setZ(int z);
}
