package net.modificationstation.stationapi.api.block;

import net.minecraft.level.Level;

@FunctionalInterface
public interface BeforeBlockRemoved {

    void beforeBlockRemoved(Level arg, int i, int j, int k);
}
