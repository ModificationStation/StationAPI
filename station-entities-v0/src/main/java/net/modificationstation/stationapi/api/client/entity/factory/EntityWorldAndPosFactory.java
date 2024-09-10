package net.modificationstation.stationapi.api.client.entity.factory;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public interface EntityWorldAndPosFactory {
    Entity create(World world, double x, double y, double z);
}
