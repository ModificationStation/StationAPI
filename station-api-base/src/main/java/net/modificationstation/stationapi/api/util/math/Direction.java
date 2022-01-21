package net.modificationstation.stationapi.api.util.math;

import com.google.common.collect.ImmutableMap;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.util.maths.Vec3i;
import net.modificationstation.stationapi.api.util.Util;

import java.util.*;

import static net.modificationstation.stationapi.api.util.math.Axis.X;
import static net.modificationstation.stationapi.api.util.math.Axis.Y;
import static net.modificationstation.stationapi.api.util.math.Axis.Z;

@RequiredArgsConstructor
public enum Direction {

    @SerializedName("down")
    DOWN("down", new Vec3i(0, -1, 0), Y),
    @SerializedName("up")
    UP("up", new Vec3i(0, 1, 0), Y),
    @SerializedName("east")
    EAST("east", new Vec3i(0, 0, -1), Z),
    @SerializedName("west")
    WEST("west", new Vec3i(0, 0, 1), Z),
    @SerializedName("north")
    NORTH("north", new Vec3i(-1, 0, 0), X),
    @SerializedName("south")
    SOUTH("south", new Vec3i(1, 0, 0), X);

    private static final ImmutableMap<String, Direction> NAME_LOOKUP = Util.createLookupBy(direction -> direction.name, values());

    static {
        DOWN.opposite = UP;
        UP.opposite = DOWN;
        EAST.opposite = WEST;
        WEST.opposite = EAST;
        NORTH.opposite = SOUTH;
        SOUTH.opposite = NORTH;
    }

    private final String name;
    public final Vec3i vector;
    @Getter
    private Direction opposite;
    public final Axis axis;

    @Override
    public String toString() {
        return name;
    }

    public static Direction byName(String name) {
        return NAME_LOOKUP.get(name.toLowerCase(Locale.ROOT));
    }
}
