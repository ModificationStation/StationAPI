package net.modificationstation.stationapi.api.client.render.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.BlockView;
import net.minecraft.util.maths.TilePos;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.render.RenderContext;
import net.modificationstation.stationapi.api.client.render.VertexConsumer;
import net.modificationstation.stationapi.api.client.render.model.json.ModelOverrideList;
import net.modificationstation.stationapi.api.client.render.model.json.ModelTransformation;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.function.Supplier;

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

    /**
     * When true, signals renderer this producer is implemented through {@link BakedModel#getQuads(BlockState, Direction, Random)}.
     * Also means the model does not rely on any non-vanilla features.
     * Allows the renderer to optimize or route vanilla models through the unmodified vanilla pipeline if desired.
     *
     * <p>Fabric overrides to true for vanilla baked models.
     * Enhanced models that use this API should return false,
     * otherwise the API will not recognize the model.
     */
    default boolean isVanillaAdapter() {
        return true;
    }

    /**
     * This method will be called during chunk rebuilds to generate both the static and
     * dynamic portions of a block model when the model implements this interface and
     * {@link #isVanillaAdapter()} returns false.
     *
     * <p>During chunk rebuild, this method will always be called exactly one time per block
     * position, irrespective of which or how many faces or block render layers are included
     * in the model. Models must output all quads/meshes in a single pass.
     *
     * <p>Also called to render block models outside of chunk rebuild or block entity rendering.
     * Typically this happens when the block is being rendered as an entity, not as a block placed in the world.
     * Currently this happens for falling blocks and blocks being pushed by a piston, but renderers
     * should invoke this for all calls to {@link BlockModelRenderer#render(BlockView, BakedModel, BlockState, TilePos, MatrixStack, VertexConsumer, boolean, Random, long, int)}
     * that occur outside of chunk rebuilds to allow for features added by mods, unless
     * {@link #isVanillaAdapter()} returns true.
     *
     * <p>Outside of chunk rebuilds, this method will be called every frame. Model implementations should
     * rely on pre-baked meshes as much as possible and keep transformation to a minimum.  The provided
     * block position may be the <em>nearest</em> block position and not actual. For this reason, neighbor
     * state lookups are best avoided or will require special handling. Block entity lookups are
     * likely to fail and/or give meaningless results.
     *
     * <p>In all cases, renderer will handle face occlusion and filter quads on faces obscured by
     * neighboring blocks (if appropriate).  Models only need to consider "sides" to the
     * extent the model is driven by connection with neighbor blocks or other world state.
     *
     * <p>Note: with {@link BakedModel#getQuads(BlockState, Direction, Random)}, the random
     * parameter is normally initialized with the same seed prior to each face layer.
     * Model authors should note this method is called only once per block, and call the provided
     * Random supplier multiple times if re-seeding is necessary. For wrapped vanilla baked models,
     * it will probably be easier to use {@link RenderContext#fallbackConsumer} which handles
     * re-seeding per face automatically.
     *
     * @param blockView Access to world state. Using {@link net.fabricmc.fabric.api.rendering.data.v1.RenderAttachedBlockView#getBlockEntityRenderAttachment(BlockPos)} to
     * retrieve block entity state unless thread safety can be guaranteed.
     * param safeBlockEntityAccessor Thread-safe access to block entity data
     * @param state Block state for model being rendered.
     * @param pos Position of block for model being rendered.
     * @param randomSupplier  Random object seeded per vanilla conventions. Call multiple times to re-seed.
     * Will not be thread-safe. Do not cache or retain a reference.
     * @param context Accepts model output.
     */
    default void emitBlockQuads(BlockView blockView, BlockState state, TilePos pos, Supplier<Random> randomSupplier, RenderContext context) {
        context.fallbackConsumer().accept(this);
    }

    /**
     * This method will be called during item rendering to generate both the static and
     * dynamic portions of an item model when the model implements this interface and
     * {@link #isVanillaAdapter()} returns false.
     *
     * <p>Vanilla item rendering is normally very limited. It ignores lightmaps, vertex colors,
     * and vertex normals. Renderers are expected to implement enhanced features for item
     * models. If a feature is impractical due to performance or other concerns, then the
     * renderer must at least give acceptable visual results without the need for special-
     * case handling in model implementations.
     *
     * <p>Calls to this method will generally happen on the main client thread but nothing
     * prevents a mod or renderer from calling this method concurrently. Implementations
     * should not mutate the ItemStack parameter, and best practice will be to make the
     * method thread-safe.
     *
     * <p>Implementing this method does NOT mitigate the need to implement a functional
     * {@link BakedModel#getOverrides()} method, because this method will be called
     * on the <em>result</em> of  {@link BakedModel#getOverrides}.  However, that
     * method can simply return the base model because the output from this method will
     * be used for rendering.
     *
     * <p>Renderer implementations should also use this method to obtain the quads used
     * for item enchantment glint rendering.  This means models can put geometric variation
     * logic here, instead of returning every possible shape from {@link BakedModel#getOverrides}
     * as vanilla baked models.
     */
    default void emitItemQuads(ItemInstance stack, Supplier<Random> randomSupplier, RenderContext context) {
        context.fallbackConsumer().accept(this);
    }
}
