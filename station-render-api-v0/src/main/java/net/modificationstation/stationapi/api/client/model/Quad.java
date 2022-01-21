package net.modificationstation.stationapi.api.client.model;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Range;

import java.util.*;
import java.util.function.*;

@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PUBLIC)
@ToString
public class Quad {

    private static final Cache<String, Quad> CACHE = Caffeine.newBuilder().softValues().build();

    @Deprecated
    Vertex v00, v01, v11, v10;
    Direction face;

    public void applyToVertexes(Consumer<Vertex> action) {
        action.accept(v00);
        action.accept(v01);
        action.accept(v11);
        action.accept(v10);
    }

    public void applyToVertexesWithIndex(ObjIntConsumer<Vertex> action) {
        action.accept(v00, 0);
        action.accept(v01, 1);
        action.accept(v11, 2);
        action.accept(v10, 3);
    }

    public Vertex vertexById(@Range(from = 0, to = 3) int id) {
        switch (id) {
            case 0:
                return v00;
            case 1:
                return v01;
            case 2:
                return v11;
            case 3:
                return v10;
            default:
                throw new IndexOutOfBoundsException("Unexpected vertex ID: " + id);
        }
    }

//    public static Quad get(
//            float fromX, float fromY, float fromZ,
//            float toX, float toY, float toZ,
//            Atlas.Sprite texture, Direction side, boolean shade, Identifier modelId
//    ) {
//        int[] vertexData;
//        switch (side) {
//            case DOWN:
//                vertexData = new int[] {
//                        Float.floatToRawIntBits(fromX), Float.floatToRawIntBits(fromY), Float.floatToRawIntBits(toZ), uv[4], uv[7], direction, shade,
//                        Float.floatToRawIntBits(fromX), Float.floatToRawIntBits(fromY), Float.floatToRawIntBits(fromZ), uv[0], uv[1], direction, shade,
//                        Float.floatToRawIntBits(toX), Float.floatToRawIntBits(fromY), Float.floatToRawIntBits(fromZ), uv[6], uv[5], direction, shade,
//                        Float.floatToRawIntBits(toX), Float.floatToRawIntBits(fromY), Float.floatToRawIntBits(toZ), uv[2], uv[3], direction, shade
//                };
//                break;
//            case UP:
//                q.add(Quad.get(
//                        Vertex.get(xTo, yTo, toZ, uv[2], uv[3], direction, shade),
//                        Vertex.get(xTo, yTo, fromZ, uv[6], uv[5], direction, shade),
//                        Vertex.get(xFrom, yTo, fromZ, uv[0], uv[1], direction, shade),
//                        Vertex.get(xFrom, yTo, toZ, uv[4], uv[7], direction, shade)
//                ));
//                break;
//            case EAST:
//                q.add(Quad.get(
//                        Vertex.get(xFrom, yTo, fromZ, uv[2], uv[1], direction, shade),
//                        Vertex.get(xTo, yTo, fromZ, uv[0], uv[1], direction, shade),
//                        Vertex.get(xTo, fromY, fromZ, uv[0], uv[3], direction, shade),
//                        Vertex.get(xFrom, fromY, fromZ, uv[2], uv[3], direction, shade)
//                ));
//                break;
//            case WEST:
//                q.add(Quad.get(
//                        Vertex.get(xFrom, yTo, toZ, uv[0], uv[1], direction, shade),
//                        Vertex.get(xFrom, fromY, toZ, uv[0], uv[3], direction, shade),
//                        Vertex.get(xTo, fromY, toZ, uv[2], uv[3], direction, shade),
//                        Vertex.get(xTo, yTo, toZ, uv[2], uv[1], direction, shade)
//                ));
//                break;
//            case NORTH:
//                q.add(Quad.get(
//                        Vertex.get(xFrom, yTo, toZ, uv[2], uv[1], direction, shade),
//                        Vertex.get(xFrom, yTo, fromZ, uv[0], uv[1], direction, shade),
//                        Vertex.get(xFrom, fromY, fromZ, uv[0], uv[3], direction, shade),
//                        Vertex.get(xFrom, fromY, toZ, uv[2], uv[3], direction, shade)
//                ));
//                break;
//            case SOUTH:
//                q.add(Quad.get(
//                        Vertex.get(xTo, fromY, toZ, uv[0], uv[3], direction, shade),
//                        Vertex.get(xTo, fromY, fromZ, uv[2], uv[3], direction, shade),
//                        Vertex.get(xTo, yTo, fromZ, uv[2], uv[1], direction, shade),
//                        Vertex.get(xTo, yTo, toZ, uv[0], uv[1], direction, shade)
//                ));
//                break;
//        }
//    }



    @Deprecated
    public static Quad get(Vertex v01, Vertex v00, Vertex v10, Vertex v11) {
        return CACHE.get(Arrays.deepToString(new Vertex[] {v01, v00, v10, v11}), cacheId -> {
            if (v00.lightingFace != v01.lightingFace || v01.lightingFace != v11.lightingFace || v11.lightingFace != v10.lightingFace)
                throw new IllegalArgumentException("All vertices must have the same face!");
            return new Quad(v01, v00, v10, v11, v00.lightingFace);
        });
    }
}
