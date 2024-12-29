package net.modificationstation.stationapi.api.client.render.model;

import com.google.common.collect.ImmutableList;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.render.model.json.ModelOverrideList;
import net.modificationstation.stationapi.api.client.render.model.json.ModelTransformation;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public interface BakedModel {

    /**
     * Returns an immutable list of model's quads, depending on the level, block position, cullface group and position-fixed random.
     *
     * @param state the {@link BlockState} that's being rendered.
     * @param face the cullface group that's being currently rendered. Null if a non-cullface group is being rendered.
     * @param random random generator that's fixed on block's position to give consistent values. Uses seed 42 if the model is rendered inside inventory.
     * @return an immutable list of model's quads, depending on the level, block position, cullface group and position-fixed random.
     */
    ImmutableList<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random);

    boolean useAmbientOcclusion();

    boolean hasDepth();

    boolean isSideLit();

    boolean isBuiltin();

    Sprite getSprite();

    ModelTransformation getTransformation();

    ModelOverrideList getOverrides();
}
