package net.modificationstation.stationapi.api.client.render.model;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.render.model.json.ModelOverrideList;
import net.modificationstation.stationapi.api.client.render.model.json.ModelTransformation;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

@Environment(EnvType.CLIENT)
public class BuiltinBakedModel implements BakedModel {
    private final ModelTransformation transformation;
    private final ModelOverrideList itemPropertyOverrides;
    private final Sprite sprite;
    private final boolean sideLit;

    public BuiltinBakedModel(ModelTransformation transformation, ModelOverrideList itemPropertyOverrides, Sprite sprite, boolean sideLit) {
        this.transformation = transformation;
        this.itemPropertyOverrides = itemPropertyOverrides;
        this.sprite = sprite;
        this.sideLit = sideLit;
    }

    public ImmutableList<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
        return ImmutableList.of();
    }

    public boolean useAmbientOcclusion() {
        return false;
    }

    public boolean hasDepth() {
        return true;
    }

    public boolean isSideLit() {
        return this.sideLit;
    }

    public boolean isBuiltin() {
        return true;
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
}
