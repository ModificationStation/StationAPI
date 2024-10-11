package net.modificationstation.stationapi.api.celestial;

import net.minecraft.world.World;

public interface WorldPropertiesWithWorld {

    void setWorld(World world);
    World getWorld();
}
