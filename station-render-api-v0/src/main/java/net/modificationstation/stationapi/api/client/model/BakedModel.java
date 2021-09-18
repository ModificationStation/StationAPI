package net.modificationstation.stationapi.api.client.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.CustomAtlasProvider;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.NotNull;

public class BakedModel implements CustomAtlasProvider {

    @NotNull
    @Getter
    private final Atlas atlas;
    @NotNull
    public final ImmutableMap<@NotNull Direction, @NotNull ImmutableList<@NotNull Vertex>> faceVertexes;
    @NotNull
    public final ImmutableList<@NotNull Vertex> vertexes;
    public final boolean ambientocclusion;

    public BakedModel(
            @NotNull Atlas atlas,
            @NotNull ImmutableMap<Direction, @NotNull ImmutableList<@NotNull Vertex>> faceVertexes,
            @NotNull ImmutableList<@NotNull Vertex> vertexes,
            boolean ambientocclusion
    ) {
        this.atlas = atlas;
        this.faceVertexes = faceVertexes;
        this.vertexes = vertexes;
        this.ambientocclusion = ambientocclusion;
    }
}
