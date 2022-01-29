package net.modificationstation.stationapi.api.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.level.BlockView;
import net.minecraft.util.maths.TilePos;
import net.modificationstation.stationapi.api.client.model.json.ModelOverrideList;
import net.modificationstation.stationapi.api.client.model.json.ModelTransformation;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public interface BakedModel {

    /**
     * Returns an immutable list of model's quads, depending on the level, block position, cullface group and position-fixed random.
     *
     * @param blockView the block view that contains the current block, or null if rendered inside an inventory.
     * @param blockPos the block's position, or null if rendered inside an inventory.
     * @param face the cullface group that's being currently rendered. Null if a non-cullface group is being rendered.
     * @param random random generator that's fixed on block's position to give consistent values. Uses seed 42 if the model is rendered inside inventory.
     * @return an immutable list of model's quads, depending on the level, block position, cullface group and position-fixed random.
     */
    ImmutableList<BakedQuad> getQuads(@Nullable BlockView blockView, @Nullable TilePos blockPos, @Nullable Direction face, Random random);

    boolean useAmbientOcclusion();

    boolean hasDepth();

    boolean isSideLit();

    boolean isBuiltin();

    Sprite getSprite();

    ModelTransformation getTransformation();

    ModelOverrideList getOverrides();
}
