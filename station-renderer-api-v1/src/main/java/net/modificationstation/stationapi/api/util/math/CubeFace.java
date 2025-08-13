package net.modificationstation.stationapi.api.util.math;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.util.Util;

@Environment(EnvType.CLIENT)
public enum CubeFace {
    DOWN(new Corner(
            DirectionIds.NORTH,
            DirectionIds.DOWN,
            DirectionIds.WEST
    ), new Corner(
            DirectionIds.NORTH,
            DirectionIds.DOWN,
            DirectionIds.EAST
    ), new Corner(
            DirectionIds.SOUTH,
            DirectionIds.DOWN,
            DirectionIds.EAST
    ), new Corner(
            DirectionIds.SOUTH,
            DirectionIds.DOWN,
            DirectionIds.WEST
    )),
    UP(new Corner(
            DirectionIds.NORTH,
            DirectionIds.UP,
            DirectionIds.EAST
    ), new Corner(
            DirectionIds.NORTH,
            DirectionIds.UP,
            DirectionIds.WEST
    ), new Corner(
            DirectionIds.SOUTH,
            DirectionIds.UP,
            DirectionIds.WEST
    ), new Corner(
            DirectionIds.SOUTH,
            DirectionIds.UP,
            DirectionIds.EAST
    )),
    EAST(new Corner(
            DirectionIds.SOUTH,
            DirectionIds.UP,
            DirectionIds.EAST
    ), new Corner(
            DirectionIds.SOUTH,
            DirectionIds.DOWN,
            DirectionIds.EAST
    ), new Corner(
            DirectionIds.NORTH,
            DirectionIds.DOWN,
            DirectionIds.EAST
    ), new Corner(
            DirectionIds.NORTH,
            DirectionIds.UP,
            DirectionIds.EAST
    )),
    WEST(new Corner(
            DirectionIds.NORTH,
            DirectionIds.UP,
            DirectionIds.WEST
    ), new Corner(
            DirectionIds.NORTH,
            DirectionIds.DOWN,
            DirectionIds.WEST
    ), new Corner(
            DirectionIds.SOUTH,
            DirectionIds.DOWN,
            DirectionIds.WEST
    ), new Corner(
            DirectionIds.SOUTH,
            DirectionIds.UP,
            DirectionIds.WEST
    )),
    NORTH(new Corner(
            DirectionIds.NORTH,
            DirectionIds.UP,
            DirectionIds.EAST
    ), new Corner(
            DirectionIds.NORTH,
            DirectionIds.DOWN,
            DirectionIds.EAST
    ), new Corner(
            DirectionIds.NORTH,
            DirectionIds.DOWN,
            DirectionIds.WEST
    ), new Corner(
            DirectionIds.NORTH,
            DirectionIds.UP,
            DirectionIds.WEST
    )),
    SOUTH(new Corner(
            DirectionIds.SOUTH,
            DirectionIds.UP,
            DirectionIds.WEST
    ), new Corner(
            DirectionIds.SOUTH,
            DirectionIds.DOWN,
            DirectionIds.WEST
    ), new Corner(
            DirectionIds.SOUTH,
            DirectionIds.DOWN,
            DirectionIds.EAST
    ), new Corner(
            DirectionIds.SOUTH,
            DirectionIds.UP,
            DirectionIds.EAST
    ));

    private static final CubeFace[] DIRECTION_LOOKUP = Util.make(new CubeFace[6], (cubeFaces) -> {
        cubeFaces[DirectionIds.DOWN] = DOWN;
        cubeFaces[DirectionIds.UP] = UP;
        cubeFaces[DirectionIds.EAST] = EAST;
        cubeFaces[DirectionIds.WEST] = WEST;
        cubeFaces[DirectionIds.NORTH] = NORTH;
        cubeFaces[DirectionIds.SOUTH] = SOUTH;
    });
    private final CubeFace.Corner[] corners;

    public static CubeFace getFace(Direction direction) {
        return DIRECTION_LOOKUP[direction.ordinal()];
    }

    CubeFace(CubeFace.Corner... corners) {
        this.corners = corners;
    }

    public CubeFace.Corner getCorner(int corner) {
        return this.corners[corner];
    }

    @Environment(EnvType.CLIENT)
    public static class Corner {
        public final int xSide;
        public final int ySide;
        public final int zSide;

        private Corner(int x, int y, int z) {
            this.xSide = x;
            this.ySide = y;
            this.zSide = z;
        }
    }

    @Environment(EnvType.CLIENT)
    public static final class DirectionIds {
        public static final int
                DOWN,
                UP,
                EAST,
                WEST,
                SOUTH,
                NORTH;

        static {
            DOWN = Direction.DOWN.ordinal();
            UP = Direction.UP.ordinal();
            EAST = Direction.EAST.ordinal();
            WEST = Direction.WEST.ordinal();
            NORTH = Direction.NORTH.ordinal();
            SOUTH = Direction.SOUTH.ordinal();
        }
    }
}
