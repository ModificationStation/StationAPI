package net.modificationstation.stationapi.api.client.render.material;

import net.modificationstation.stationapi.api.client.render.RenderContext;
import net.modificationstation.stationapi.api.client.render.Renderer;
import net.modificationstation.stationapi.api.client.render.mesh.QuadEmitter;

/**
 * Finds standard {@link RenderMaterial} instances used to communicate
 * quad rendering characteristics to a {@link RenderContext}.
 *
 * <p>Must be obtained via {@link Renderer#materialFinder()}.
 */
public interface MaterialFinder {
    /**
     * Returns the standard material encoding all
     * of the current settings in this finder. The settings in
     * this finder are not changed.
     *
     * <p>Resulting instances can and should be re-used to prevent
     * needless memory allocation. {@link Renderer} implementations
     * may or may not cache standard material instances.
     */
    RenderMaterial find();

    /**
     * Resets this instance to default values. Values will match those
     * in effect when an instance is newly obtained via {@link Renderer#materialFinder()}.
     */
    MaterialFinder clear();

    /**
     * Reserved for future use.  Behavior for values &gt; 1 is currently undefined.
     */
    MaterialFinder spriteDepth(int depth);

    /**
     * Defines how sprite pixels will be blended with the scene.
     *
     * <p>See {@link BlendMode} for more information.
     */
    MaterialFinder blendMode(int spriteIndex, BlendMode blendMode);

    /**
     * Vertex color(s) will be modified for quad color index unless disabled.
     */
    MaterialFinder disableColorIndex(int spriteIndex, boolean disable);

    /**
     * Vertex color(s) will be modified for diffuse shading unless disabled.
     */
    MaterialFinder disableDiffuse(int spriteIndex, boolean disable);

    /**
     * Vertex color(s) will be modified for ambient occlusion unless disabled.
     */
    MaterialFinder disableAo(int spriteIndex, boolean disable);

    /**
     * When true, sprite texture and color will be rendered at full brightness.
     * Lightmap values provided via {@link QuadEmitter#lightmap(int)} will be ignored.
     * False by default
     *
     * <p>This is the preferred method for emissive lighting effects.  Some renderers
     * with advanced lighting models may not use block lightmaps and this method will
     * allow per-sprite emissive lighting in future extensions that support overlay sprites.
     *
     * <p>Note that color will still be modified by diffuse shading and ambient occlusion,
     * unless disabled via {@link #disableAo(int, boolean)} and {@link #disableDiffuse(int, boolean)}.
     */
    MaterialFinder emissive(int spriteIndex, boolean isEmissive);
}