package net.modificationstation.stationapi.api.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.level.BlockView;
import net.minecraft.util.Vec3i;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public interface BakedModel {

    /**
     * Returns an immutable list of model's vertexes, depending on the level, block position, cullface group and position-fixed random.
     *
     * @deprecated this shouldn't be used, because all vertexes are stitched into quads each time the model is rendered anyways, which worsens the performance.
     *
     * @param blockView the block view that contains the current block, or null if rendered inside an inventory.
     * @param blockPos the block's position, or null if rendered inside an inventory.
     * @param face the cullface group that's being currently rendered. Null if a non-cullface group is being rendered.
     * @param random random generator that's fixed on block's position to give consistent values. Uses seed 42 if the model is rendered inside inventory.
     * @return an immutable list of model's vertexes, depending on the level, block position, cullface group and position-fixed random.
     */
    @Deprecated
    default ImmutableList<Vertex> getVertexes(@Nullable BlockView blockView, @Nullable Vec3i blockPos, @Nullable Direction face, Random random) {
        return Vertex.fromQuads(getQuads(blockView, blockPos, face, random));
    }

    /**
     * Returns an immutable list of model's quads, depending on the level, block position, cullface group and position-fixed random.
     *
     * @param blockView the block view that contains the current block, or null if rendered inside an inventory.
     * @param blockPos the block's position, or null if rendered inside an inventory.
     * @param face the cullface group that's being currently rendered. Null if a non-cullface group is being rendered.
     * @param random random generator that's fixed on block's position to give consistent values. Uses seed 42 if the model is rendered inside inventory.
     * @return an immutable list of model's quads, depending on the level, block position, cullface group and position-fixed random.
     */
    default ImmutableList<Quad> getQuads(@Nullable BlockView blockView, @Nullable Vec3i blockPos, @Nullable Direction face, Random random) {
        return Quad.fromVertexes(getVertexes(blockView, blockPos, face, random));
    }

    boolean useAmbientOcclusion();

    boolean hasDepth();

    boolean isSideLit();

    boolean isBuiltin();

    Atlas.Sprite getSprite();

    ModelTransformation getTransformation();

    ModelOverrideList getOverrides();
}
