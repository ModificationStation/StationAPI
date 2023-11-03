package net.modificationstation.stationapi.api.block;

import net.minecraft.world.World;

@FunctionalInterface
public interface BeforeBlockRemoved {

    void beforeBlockRemoved(World arg, int i, int j, int k);
}
