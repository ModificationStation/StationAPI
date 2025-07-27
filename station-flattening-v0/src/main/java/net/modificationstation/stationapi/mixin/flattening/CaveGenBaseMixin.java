package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.world.World;
import net.minecraft.world.gen.carver.Carver;
import net.modificationstation.stationapi.impl.world.CaveGenBaseImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Carver.class)
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
        if (stationapi_world == null) {
            throw new RuntimeException("stationapi_world is null, use CaveGenBaseImpl.stationapi_setWorld in your custom ChunkSource constructor to fix.");
        }
        return stationapi_world;
    }
}
