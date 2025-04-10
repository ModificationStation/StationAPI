package net.modificationstation.stationapi.api.client.render.model;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.client.render.Renderer;
import net.modificationstation.stationapi.api.client.render.mesh.Mesh;
import net.modificationstation.stationapi.api.client.render.mesh.MutableMesh;
import net.modificationstation.stationapi.api.client.render.mesh.QuadEmitter;
import net.modificationstation.stationapi.api.client.render.model.json.JsonUnbakedModel;
import net.modificationstation.stationapi.api.client.render.model.json.ModelOverrideList;
import net.modificationstation.stationapi.api.client.render.model.json.ModelTransformation;
import net.modificationstation.stationapi.api.client.texture.Sprite;

@Environment(EnvType.CLIENT)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class BasicBakedModel implements BakedModel {
    protected final Mesh mesh;
    protected final boolean usesAo;
    protected final boolean hasDepth;
    protected final boolean isSideLit;
    protected final Sprite sprite;
    protected final ModelTransformation transformation;
    protected final ModelOverrideList itemPropertyOverrides;

    @Override
    public void emitBlockQuads(BlockInputContext input, QuadEmitter output) {
        this.mesh.outputTo(output);
    }

    @Override
    public void emitItemQuads(ItemInputContext input, QuadEmitter output) {
        this.mesh.outputTo(output);
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
        private final MutableMesh mesh;
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
            this.mesh = Renderer.get().mutableMesh();
            this.itemPropertyOverrides = modelOverrideList;
            this.usesAo = usesAo;
            this.isSideLit = isSideLit;
            this.hasDepth = hasDepth;
            this.transformation = modelTransformation;
        }

        public QuadEmitter quadEmitter() {
            return this.mesh.emitter();
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
                        mesh.immutableCopy(),
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
