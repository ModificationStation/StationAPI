package net.modificationstation.stationapi.impl.client.model;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.modificationstation.stationapi.api.block.BlockFaces;

import java.util.*;
import java.util.concurrent.*;

@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PUBLIC)
public class FaceQuadPoint {

    private static final Cache<String, FaceQuadPoint> CACHE = CacheBuilder.newBuilder().softValues().build();

    double
            x, y, z,
            u, v;
    BlockFaces face;

    public static FaceQuadPoint get(double x, double y, double z, double u, double v, BlockFaces face) {
        try {
            return CACHE.get(Arrays.deepToString(new Object[] {x, y, z, u, v, face}), () -> new FaceQuadPoint(x, y, z, u, v, face));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
