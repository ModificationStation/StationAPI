package net.modificationstation.stationapi.api.client.model;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.modificationstation.stationapi.api.util.math.Direction;

import java.util.*;
import java.util.concurrent.*;

@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PUBLIC)
public class Vertex {

    private static final Cache<String, Vertex> CACHE = CacheBuilder.newBuilder().softValues().build();

    double
            x, y, z,
            u, v;
    Direction lightingFace;
    float normalX, normalY, normalZ;
    boolean shade;

    public static Vertex get(double x, double y, double z, double u, double v, Direction face, boolean shade) {
        return get(x, y, z, u, v, face, face, shade);
    }

    public static Vertex get(double x, double y, double z, double u, double v, Direction lightingFace, Direction normal, boolean shade) {
        return get(x, y, z, u, v, lightingFace, normal.vector.x, normal.vector.y, normal.vector.z, shade);
    }

    public static Vertex get(double x, double y, double z, double u, double v, Direction face, float normalX, float normalY, float normalZ, boolean shade) {
        try {
            return CACHE.get(Arrays.deepToString(new Object[] {x, y, z, u, v, face, normalX, normalY, normalZ, shade}), () -> new Vertex(x, y, z, u, v, face, normalX, normalY, normalZ, shade));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
