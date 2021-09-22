package net.modificationstation.stationapi.api.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.level.BlockView;
import net.minecraft.util.Vec3i;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public interface BakedModel {

    ImmutableList<Vertex> getVertexes(@Nullable BlockView blockView, @Nullable Vec3i blockPos, @Nullable Direction face, Random random);

    boolean useAmbientOcclusion();

    boolean hasDepth();

    boolean isSideLit();

    boolean isBuiltin();

    Atlas.Sprite getSprite();

    ModelTransformation getTransformation();

    ModelOverrideList getOverrides();
}
