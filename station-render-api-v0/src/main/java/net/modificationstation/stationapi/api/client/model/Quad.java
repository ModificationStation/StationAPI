package net.modificationstation.stationapi.api.client.model;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PUBLIC)
@ToString
public class Quad {

    private static final Cache<String, Quad> CACHE = CacheBuilder.newBuilder().softValues().build();

    Vertex v00, v01, v11, v10;

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

    public static Quad get(Vertex v01, Vertex v00, Vertex v10, Vertex v11) {
        try {
            return CACHE.get(Arrays.deepToString(new Vertex[] {v01, v00, v10, v11}), () -> new Quad(v01, v00, v10, v11));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
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
