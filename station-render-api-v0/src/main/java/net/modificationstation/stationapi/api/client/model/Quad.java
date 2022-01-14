package net.modificationstation.stationapi.api.client.model;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.ImmutableList;
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

    Vertex v00, v01, v11, v10;
    Direction lightingFace;

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

    public static Quad get(Vertex v01, Vertex v00, Vertex v10, Vertex v11) {
        return CACHE.get(Arrays.deepToString(new Vertex[] {v01, v00, v10, v11}), cacheId -> {
            if (v00.lightingFace != v01.lightingFace || v01.lightingFace != v11.lightingFace || v11.lightingFace != v10.lightingFace)
                throw new IllegalArgumentException("All vertices must have the same lighting face!");
            return new Quad(v01, v00, v10, v11, v00.lightingFace);
        });
    }

    public static ImmutableList<Quad> fromVertexes(ImmutableList<Vertex> v) {
        int size = v.size();
        if (size % 4 == 0) {
            ImmutableList.Builder<Quad> q = ImmutableList.builder();
            for (int i = 0; i < size; i += 4)
                q.add(get(v.get(i), v.get(i + 1), v.get(i + 2), v.get(i + 3)));
            return q.build();
        } else
            throw new IllegalArgumentException("The size of vertex's list is not a multiple of 4!");
    }
}
