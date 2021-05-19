package net.modificationstation.stationapi.api.block;

public interface BlockHardnessPerMeta {

    // TODO: provide TileView and coords instead of meta.
    float getHardness(int meta);
}
