package net.modificationstation.stationapi.api.block;

public enum BlockFaces {
    DOWN,
    UP,
    EAST,
    WEST,
    NORTH,
    SOUTH;

    public BlockFaces getOpposite() {
        return values()[ordinal() + ordinal() % 2 == 0 ? 1 : -1];
    }
}
