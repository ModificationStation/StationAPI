package net.modificationstation.stationapi.impl.client.arsenic.renderer.render.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.BlockView;
import net.minecraft.util.maths.TilePos;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.render.RenderContext;
import net.modificationstation.stationapi.api.client.render.mesh.Mesh;
import net.modificationstation.stationapi.api.client.render.model.BakedModel;
import net.modificationstation.stationapi.api.client.render.model.BakedQuad;
import net.modificationstation.stationapi.api.client.render.model.json.ModelOverrideList;
import net.modificationstation.stationapi.api.client.render.model.json.ModelTransformation;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.function.Supplier;

@SuppressWarnings("ClassCanBeRecord")
final class VanillaBakedModel implements BakedModel {

    private final BlockBase block;
    private final Mesh mesh;
    private final Sprite particle;
    private final ModelTransformation transformation;

    VanillaBakedModel(BlockBase block, Mesh mesh, Sprite particle, ModelTransformation transformation) {
        this.block = block;
        this.mesh = mesh;
        this.particle = particle;
        this.transformation = transformation;
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockView blockView, BlockState state, TilePos pos, Supplier<Random> randomSupplier, RenderContext context) {
        context.meshConsumer().accept(mesh);
    }

    @Override
    public void emitItemQuads(ItemInstance stack, Supplier<Random> randomSupplier, RenderContext context) {
        context.meshConsumer().accept(mesh);
    }

    @Override
    public ImmutableList<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
        return ImmutableList.of();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean hasDepth() {
        return false;
    }

    @Override
    public boolean isSideLit() {
        return false;
    }

    @Override
    public boolean isBuiltin() {
        return false;
    }

    @Override
    public Sprite getSprite() {
        return particle;
    }

    @Override
    public ModelTransformation getTransformation() {
        return transformation;
    }

    @Override
    public ModelOverrideList getOverrides() {
        return ModelOverrideList.EMPTY;
    }
}
