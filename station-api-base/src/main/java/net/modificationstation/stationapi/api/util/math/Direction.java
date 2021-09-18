package net.modificationstation.stationapi.api.util.math;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.util.maths.Vec3i;

import static net.modificationstation.stationapi.api.util.math.Axis.X;
import static net.modificationstation.stationapi.api.util.math.Axis.Y;
import static net.modificationstation.stationapi.api.util.math.Axis.Z;

@RequiredArgsConstructor
public enum Direction {

    @SerializedName("down")
    DOWN(new Vec3i(0, -1, 0), Y),
    @SerializedName("up")
    UP(new Vec3i(0, 1, 0), Y),
    @SerializedName("east")
    EAST(new Vec3i(0, 0, -1), Z),
    @SerializedName("west")
    WEST(new Vec3i(0, 0, 1), Z),
    @SerializedName("north")
    NORTH(new Vec3i(-1, 0, 0), X),
    @SerializedName("south")
    SOUTH(new Vec3i(1, 0, 0), X);

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
    public final Axis axis;
}
