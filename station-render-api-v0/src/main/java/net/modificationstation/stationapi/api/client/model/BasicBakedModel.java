package net.modificationstation.stationapi.api.client.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import net.minecraft.level.BlockView;
import net.minecraft.util.Vec3i;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class BasicBakedModel implements BakedModel {

    @NotNull
    private final ImmutableMap<@NotNull Direction, @NotNull ImmutableList<@NotNull Vertex>> faceVertexes;
    @NotNull
    private final ImmutableList<@NotNull Vertex> vertexes;
    private final boolean ambientocclusion;
    @Getter
    private final boolean isSideLit;
    @Getter
    private final Atlas.Sprite sprite;
    @Getter
    private final ModelTransformation transformation;
    @Getter
    private final ModelOverrideList overrides;

    private BasicBakedModel(
            final @NotNull ImmutableMap<@NotNull Direction, @NotNull ImmutableList<@NotNull Vertex>> faceVertexes,
            final @NotNull ImmutableList<@NotNull Vertex> vertexes,
            final boolean ambientocclusion,
            final boolean isSideLit,
            final @NotNull Atlas.Sprite sprite,
            ModelTransformation transformation,
            ModelOverrideList overrides
    ) {
        this.faceVertexes = faceVertexes;
        this.vertexes = vertexes;
        this.ambientocclusion = ambientocclusion;
        this.sprite = sprite;
        this.isSideLit = isSideLit;
        this.transformation = transformation;
        this.overrides = overrides;
    }

    @Override
    public ImmutableList<Vertex> getVertexes(@Nullable BlockView blockView, @Nullable Vec3i blockPos, @Nullable Direction face, Random random) {
        return face == null ? vertexes : faceVertexes.get(face);
    }

    @Override
    public boolean useAmbientOcclusion() {
        return ambientocclusion;
    }

    @Override
    public boolean hasDepth() {
        return true;
    }

    @Override
    public boolean isBuiltin() {
        return false;
    }

    public final static class Builder {

        private ImmutableMap<Direction, ImmutableList<Vertex>> faceVertexes = ImmutableMap.of();
        private ImmutableList<Vertex> vertexes = ImmutableList.of();
        private boolean useAO = true;
        private boolean isSideLit = true;
        private Atlas.Sprite sprite;
        private ModelTransformation transformation = null;
        private ModelOverrideList overrides = null;

        public Builder faceVertexes(ImmutableMap<Direction, ImmutableList<Vertex>> faceVertexes) {
            this.faceVertexes = faceVertexes;
            return this;
        }

        public Builder vertexes(ImmutableList<Vertex> vertexes) {
            this.vertexes = vertexes;
            return this;
        }

        public Builder useAO(boolean useAO) {
            this.useAO = useAO;
            return this;
        }

        public Builder isSideLit(boolean isSideLit) {
            this.isSideLit = isSideLit;
            return this;
        }

        public Builder sprite(Atlas.Sprite sprite) {
            this.sprite = sprite;
            return this;
        }

        public Builder transformation(ModelTransformation transformation) {
            this.transformation = transformation;
            return this;
        }

        public Builder overrides(ModelOverrideList overrides) {
            this.overrides = overrides;
            return this;
        }

        public BasicBakedModel build() {
            if (sprite == null)
                throw new IllegalStateException("Sprite wasn't defined in the BasicBakedModel builder!");
            return new BasicBakedModel(
                    faceVertexes,
                    vertexes,
                    useAO,
                    isSideLit,
                    sprite,
                    transformation,
                    overrides
            );
        }
    }
}
