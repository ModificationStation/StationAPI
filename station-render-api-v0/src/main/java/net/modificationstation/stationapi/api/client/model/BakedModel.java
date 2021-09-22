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
    public final Atlas.Sprite sprite;

    public BakedModel(
            final @NotNull Atlas atlas,
            final @NotNull ImmutableMap<@NotNull Direction, @NotNull ImmutableList<@NotNull Vertex>> faceVertexes,
            final @NotNull ImmutableList<@NotNull Vertex> vertexes,
            final boolean ambientocclusion,
            final @NotNull Atlas.Sprite sprite
    ) {
        this.atlas = atlas;
        this.faceVertexes = faceVertexes;
        this.vertexes = vertexes;
        this.ambientocclusion = ambientocclusion;
        this.sprite = sprite;
    }
}
