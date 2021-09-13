package net.modificationstation.stationapi.api.block;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import net.minecraft.util.maths.Vec3i;

public enum Direction {

    @SerializedName("down")
    DOWN(new Vec3i(0, -1, 0)),
    @SerializedName("up")
    UP(new Vec3i(0, 1, 0)),
    @SerializedName("east")
    EAST(new Vec3i(0, 0, -1)),
    @SerializedName("west")
    WEST(new Vec3i(0, 0, 1)),
    @SerializedName("north")
    NORTH(new Vec3i(-1, 0, 0)),
    @SerializedName("south")
    SOUTH(new Vec3i(1, 0, 0));

    static {
        DOWN.opposite = UP;
        UP.opposite = DOWN;
        EAST.opposite = WEST;
        WEST.opposite = EAST;
        NORTH.opposite = SOUTH;
        SOUTH.opposite = NORTH;
    }

    public final Vec3i vector;
    @Getter
    private Direction opposite;

    Direction(Vec3i vector) {
        this.vector = vector;
    }
}
