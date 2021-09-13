package net.modificationstation.stationapi.api.client.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import net.modificationstation.stationapi.api.block.Direction;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.CustomAtlasProvider;
import org.jetbrains.annotations.NotNull;

public class BakedModel implements CustomAtlasProvider {

    @NotNull
    @Getter
    private final Atlas atlas;
    @NotNull
    public final ImmutableMap<@NotNull Direction, @NotNull ImmutableList<@NotNull Vertex>> faceVertexes;
    @NotNull
    public final ImmutableList<@NotNull Vertex> vertexes;

    public BakedModel(
            @NotNull Atlas atlas,
            @NotNull ImmutableMap<Direction, @NotNull ImmutableList<@NotNull Vertex>> faceVertexes,
            @NotNull ImmutableList<@NotNull Vertex> vertexes
    ) {
        this.atlas = atlas;
        this.faceVertexes = faceVertexes;
        this.vertexes = vertexes;
    }
}
