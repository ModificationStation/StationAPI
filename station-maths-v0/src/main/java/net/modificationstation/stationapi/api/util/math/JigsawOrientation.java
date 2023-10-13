package net.modificationstation.stationapi.api.util.math;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.modificationstation.stationapi.api.util.StringIdentifiable;

public enum JigsawOrientation implements StringIdentifiable {
    DOWN_EAST("down_east", Direction.DOWN, Direction.EAST),
    DOWN_NORTH("down_north", Direction.DOWN, Direction.NORTH),
    DOWN_SOUTH("down_south", Direction.DOWN, Direction.SOUTH),
    DOWN_WEST("down_west", Direction.DOWN, Direction.WEST),
    UP_EAST("up_east", Direction.UP, Direction.EAST),
    UP_NORTH("up_north", Direction.UP, Direction.NORTH),
    UP_SOUTH("up_south", Direction.UP, Direction.SOUTH),
    UP_WEST("up_west", Direction.UP, Direction.WEST),
    WEST_UP("west_up", Direction.WEST, Direction.UP),
    EAST_UP("east_up", Direction.EAST, Direction.UP),
    NORTH_UP("north_up", Direction.NORTH, Direction.UP),
    SOUTH_UP("south_up", Direction.SOUTH, Direction.UP);

    private static final Int2ObjectMap<JigsawOrientation> BY_INDEX = new Int2ObjectOpenHashMap<>(values().length);
    private final String name;
    private final Direction rotation;
    private final Direction facing;

    private static int getIndex(Direction facing, Direction rotation) {
        return facing.ordinal() << 3 | rotation.ordinal();
    }

    JigsawOrientation(String name, Direction facing, Direction rotation) {
        this.name = name;
        this.facing = facing;
        this.rotation = rotation;
    }

    @Override
    public String asString() {
        return this.name;
    }

    public static JigsawOrientation byDirections(Direction facing, Direction rotation) {
        int i = getIndex(rotation, facing);
        return BY_INDEX.get(i);
    }

    public Direction getFacing() {
        return this.facing;
    }

    public Direction getRotation() {
        return this.rotation;
    }

    static {
        JigsawOrientation[] var0 = values();

        for (JigsawOrientation jigsawOrientation : var0) {
            BY_INDEX.put(getIndex(jigsawOrientation.rotation, jigsawOrientation.facing), jigsawOrientation);
        }

    }
}
