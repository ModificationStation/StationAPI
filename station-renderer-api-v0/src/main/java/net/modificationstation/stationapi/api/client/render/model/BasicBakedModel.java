package net.modificationstation.stationapi.api.client.render.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.render.model.json.JsonUnbakedModel;
import net.modificationstation.stationapi.api.client.render.model.json.ModelOverrideList;
import net.modificationstation.stationapi.api.client.render.model.json.ModelTransformation;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

@Environment(EnvType.CLIENT)
public class BasicBakedModel implements BakedModel {
    protected final ImmutableList<BakedQuad> quads;
    protected final ImmutableMap<Direction, ImmutableList<BakedQuad>> faceQuads;
    protected final boolean usesAo;
    protected final boolean hasDepth;
    protected final boolean isSideLit;
    protected final Sprite sprite;
    protected final ModelTransformation transformation;
    protected final ModelOverrideList itemPropertyOverrides;

    private BasicBakedModel(ImmutableList<BakedQuad> quads, ImmutableMap<Direction, ImmutableList<BakedQuad>> faceQuads, boolean usesAo, boolean isSideLit, boolean hasDepth, Sprite sprite, ModelTransformation modelTransformation, ModelOverrideList modelOverrideList) {
        this.quads = quads;
        this.faceQuads = faceQuads;
        this.usesAo = usesAo;
        this.hasDepth = hasDepth;
        this.isSideLit = isSideLit;
        this.sprite = sprite;
        this.transformation = modelTransformation;
        this.itemPropertyOverrides = modelOverrideList;
    }

    @Override
    public ImmutableList<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
        return face == null ? this.quads : this.faceQuads.get(face);
    }

    public boolean useAmbientOcclusion() {
        return this.usesAo;
    }

    public boolean hasDepth() {
        return this.hasDepth;
    }

    public boolean isSideLit() {
        return this.isSideLit;
    }

    public boolean isBuiltin() {
        return false;
    }

    public Sprite getSprite() {
        return this.sprite;
    }

    public ModelTransformation getTransformation() {
        return this.transformation;
    }

    public ModelOverrideList getOverrides() {
        return this.itemPropertyOverrides;
    }

    @Environment(EnvType.CLIENT)
    public static class Builder {
        private final ImmutableList.Builder<BakedQuad> quads;
        private final Map<Direction, ImmutableList.Builder<BakedQuad>> faceQuads;
        private final ModelOverrideList itemPropertyOverrides;
        private final boolean usesAo;
        private Sprite particleTexture;
        private final boolean isSideLit;
        private final boolean hasDepth;
        private final ModelTransformation transformation;

        public Builder(JsonUnbakedModel unbakedModel, ModelOverrideList itemPropertyOverrides, boolean hasDepth) {
            this(unbakedModel.useAmbientOcclusion(), unbakedModel.getGuiLight().isSide(), hasDepth, unbakedModel.getTransformations(), itemPropertyOverrides);
        }

        private Builder(boolean usesAo, boolean isSideLit, boolean hasDepth, ModelTransformation modelTransformation, ModelOverrideList modelOverrideList) {
            this.quads = ImmutableList.builder();
            this.faceQuads = new EnumMap<>(Direction.class);
            Direction[] var6 = Direction.values();

            for (Direction direction : var6) {
                this.faceQuads.put(direction, ImmutableList.builder());
            }

            this.itemPropertyOverrides = modelOverrideList;
            this.usesAo = usesAo;
            this.isSideLit = isSideLit;
            this.hasDepth = hasDepth;
            this.transformation = modelTransformation;
        }

        public BasicBakedModel.Builder addQuad(Direction side, BakedQuad quad) {
            this.faceQuads.get(side).add(quad);
            return this;
        }

        public BasicBakedModel.Builder addQuad(BakedQuad quad) {
            this.quads.add(quad);
            return this;
        }

        public BasicBakedModel.Builder setParticle(Sprite sprite) {
            this.particleTexture = sprite;
            return this;
        }

        public BakedModel build() {
            if (this.particleTexture == null) {
                throw new RuntimeException("Missing particle!");
            } else {
                return new BasicBakedModel(this.quads.build(), this.faceQuads.entrySet().stream().collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, e -> e.getValue().build())), this.usesAo, this.isSideLit, this.hasDepth, this.particleTexture, this.transformation, this.itemPropertyOverrides);
            }
        }
    }
}
