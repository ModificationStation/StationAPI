package net.modificationstation.stationapi.api.client.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import lombok.Getter;
import net.minecraft.level.BlockView;
import net.minecraft.util.Vec3i;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.*;

public class BasicBakedModel implements BakedModel {

    @NotNull
    private final ImmutableMap<@NotNull Direction, @NotNull ImmutableList<@NotNull Quad>> faceQuads;
    @NotNull
    private final ImmutableList<@NotNull Quad> quads;
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
            final @NotNull ImmutableMap<@NotNull Direction, @NotNull ImmutableList<@NotNull Quad>> faceQuads,
            final @NotNull ImmutableList<@NotNull Quad> quads,
            final boolean ambientocclusion,
            final boolean isSideLit,
            final @NotNull Atlas.Sprite sprite,
            ModelTransformation transformation,
            ModelOverrideList overrides
    ) {
        this.faceQuads = faceQuads;
        this.quads = quads;
        this.ambientocclusion = ambientocclusion;
        this.sprite = sprite;
        this.isSideLit = isSideLit;
        this.transformation = transformation;
        this.overrides = overrides;
    }

    @Override
    public ImmutableList<Quad> getQuads(@Nullable BlockView blockView, @Nullable Vec3i blockPos, @Nullable Direction face, Random random) {
        return face == null ? quads : faceQuads.get(face);
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

        private ImmutableMap<Direction, ImmutableList<Quad>> faceQuads = ImmutableMap.of();
        private ImmutableList<Quad> quads = ImmutableList.of();
        private boolean useAO = true;
        private boolean isSideLit = true;
        private Atlas.Sprite sprite;
        private ModelTransformation transformation = null;
        private ModelOverrideList overrides = null;

        /** @deprecated use {@link Builder#faceQuads(ImmutableMap)} instead */
        @Deprecated
        public Builder faceVertexes(ImmutableMap<Direction, ImmutableList<Vertex>> faceVertexes) {
            this.faceQuads = Maps.immutableEnumMap(faceVertexes.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> Quad.fromVertexes(e.getValue()), (l, r) -> { throw new IllegalArgumentException("Duplicate keys " + l + "and " + r + "."); }, () -> new EnumMap<>(Direction.class))));
            return this;
        }

        public Builder faceQuads(ImmutableMap<Direction, ImmutableList<Quad>> faceQuads) {
            this.faceQuads = faceQuads;
            return this;
        }

        /** @deprecated use {@link Builder#quads(ImmutableList)} instead */
        @Deprecated
        public Builder vertexes(ImmutableList<Vertex> vertexes) {
            this.quads = Quad.fromVertexes(vertexes);
            return this;
        }

        public Builder quads(ImmutableList<Quad> quads) {
            this.quads = quads;
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
                    faceQuads,
                    quads,
                    useAO,
                    isSideLit,
                    sprite,
                    transformation,
                    overrides
            );
        }
    }
}
