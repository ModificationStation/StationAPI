package net.modificationstation.stationapi.api.client.render.material;

import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.render.Renderer;
import net.modificationstation.stationapi.api.util.TriState;

public interface MaterialFinder extends MaterialView {
    /**
     * When true, sprite texture and color will be rendered at full brightness.
     * Lightmap values provided via {@link QuadEmitter#lightmap(int)} will be ignored.
     *
     * <p>This is the preferred method for emissive lighting effects. Some renderers
     * with advanced lighting pipelines may not use block lightmaps and this method will
     * allow per-sprite emissive lighting in future extensions that support overlay sprites.
     *
     * <p>Note that color will still be modified by diffuse shading and ambient occlusion,
     * unless disabled via {@link #disableDiffuse(boolean)} and {@link #ambientOcclusion(TriState)}.
     *
     * <p>The default value is {@code false}.
     */
    MaterialFinder emissive(boolean isEmissive);

    /**
     * Controls whether vertex colors should be modified for diffuse shading. This property
     * is inverted, so a value of {@code false} means that diffuse shading will be applied.
     *
     * <p>The default value is {@code false}.
     *
     * <p>This property is guaranteed to be respected in block contexts. Some renderers may also respect it in item
     * contexts, but this is not guaranteed.
     */
    MaterialFinder disableDiffuse(boolean disable);

    /**
     * Controls whether vertex colors should be modified for ambient occlusion.
     *
     * <p>If set to {@link TriState#UNSET}, ambient occlusion will be used if the block state has
     * {@linkplain BlockState#getLuminance() a luminance} of 0. Set to {@link TriState#TRUE} or {@link TriState#FALSE}
     * to override this behavior. {@link TriState#TRUE} will not have an effect if
     * {@linkplain Minecraft#method_2148()} () ambient occlusion is disabled globally}.
     *
     * <p>The default value is {@link TriState#UNSET}.
     *
     * <p>This property is respected only in block contexts. It will not have an effect in other contexts.
     */
    MaterialFinder ambientOcclusion(TriState mode);

    /**
     * A hint to the renderer about how the quad is intended to be shaded, for example through ambient occlusion and
     * diffuse shading. The renderer is free to ignore this hint.
     *
     * <p>The default value is {@link ShadeMode#ENHANCED}.
     *
     * <p>This property is respected only in block contexts. It will not have an effect in other contexts.
     *
     * @see ShadeMode
     */
    MaterialFinder shadeMode(ShadeMode mode);

    /**
     * Copies all properties from the given {@link MaterialView} to this material finder.
     */
    MaterialFinder copyFrom(MaterialView material);

    /**
     * Resets this instance to default values. Values will match those in effect when an instance is newly obtained via
     * {@link Renderer#materialFinder()}.
     */
    MaterialFinder clear();

    /**
     * Returns the standard material encoding all the current settings in this finder. The settings in this finder are
     * not changed.
     *
     * <p>Resulting instances can and should be re-used to prevent needless memory allocation. {@link Renderer}
     * implementations may or may not cache standard material instances.
     */
    RenderMaterial find();
}
