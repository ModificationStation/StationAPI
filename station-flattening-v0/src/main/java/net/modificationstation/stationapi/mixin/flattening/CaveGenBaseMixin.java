package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.class_105;
import net.minecraft.world.World;
import net.modificationstation.stationapi.impl.world.CaveGenBaseImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(class_105.class)
class CaveGenBaseMixin implements CaveGenBaseImpl {
    @Unique
    private World stationapi_world;

    @Override
    @Unique
    public void stationapi_setWorld(World world) {
        stationapi_world = world;
    }

    @Override
    @Unique
    public World stationapi_getWorld() {
        return stationapi_world;
    }
}
