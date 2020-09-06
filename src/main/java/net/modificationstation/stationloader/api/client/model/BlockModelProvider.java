package net.modificationstation.stationloader.api.client.model;

import net.minecraft.level.Level;

public interface BlockModelProvider {

    CustomModel getCustomWorldModel(Level level, int x, int y, int z, int meta);

    CustomModel getCustomInventoryModel(int meta);
}
