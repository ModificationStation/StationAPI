package net.modificationstation.stationapi.api.client.render.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class BasicBakedModel implements BakedModel {
    protected final ImmutableList<BakedQuad> quads;
    protected final ImmutableMap<Direction, ImmutableList<BakedQuad>> faceQuads;
    protected final boolean usesAo;
    protected final boolean hasDepth;
    protected final boolean isSideLit;
    protected final Sprite sprite;
    protected final ModelTransformation transformation;
    protected final ModelOverrideList itemPropertyOverrides;

    @Override
    public ImmutableList<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
        return face == null ? this.quads : this.faceQuads.get(face);
    }

    @Override
    public boolean useAmbientOcclusion() {
        return this.usesAo;
    }

    @Override
    public boolean hasDepth() {
        return this.hasDepth;
    }

    @Override
    public boolean isSideLit() {
        return this.isSideLit;
    }

    @Override
    public boolean isBuiltin() {
        return false;
    }

    @Override
    public Sprite getSprite() {
        return this.sprite;
    }

    @Override
    public ModelTransformation getTransformation() {
        return this.transformation;
    }

    @Override
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

        public Builder(boolean usesAo, boolean isSideLit, boolean hasDepth, ModelTransformation modelTransformation, ModelOverrideList modelOverrideList) {
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
            if (particleTexture == null) {
                throw new RuntimeException("Missing particle!");
            } else {
                return new BasicBakedModel(
                        quads.build(),
                        faceQuads.entrySet().stream().collect(Maps.toImmutableEnumMap(Map.Entry::getKey, e -> e.getValue().build())),
                        usesAo,
                        hasDepth,
                        isSideLit,
                        particleTexture,
                        transformation,
                        itemPropertyOverrides
                );
            }
        }
    }
}
