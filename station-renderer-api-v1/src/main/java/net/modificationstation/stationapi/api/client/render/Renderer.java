package net.modificationstation.stationapi.api.client.render;

import net.modificationstation.stationapi.api.client.render.model.BakedModelRenderer;
import net.modificationstation.stationapi.impl.client.render.RendererManager;

/**
 * Interface for rendering plug-ins that provide enhanced capabilities
 * for model lighting, buffering and rendering. Such plug-ins implement the
 * enhanced model rendering interfaces specified by the Fabric API.
 */
public interface Renderer {
    /**
     * Access to the current {@link Renderer} for creating and retrieving mesh builders
     * and materials.
     */
    static Renderer get() {
        return RendererManager.getRenderer();
    }

    /**
     * Rendering extension mods must implement {@link Renderer} and
     * call this method during initialization.
     *
     * <p>Only one {@link Renderer} plug-in can be active in any game instance.
     * If a second mod attempts to register, this method will throw an UnsupportedOperationException.
     */
    static void register(Renderer renderer) {
        RendererManager.registerRenderer(renderer);
    }

    /**
     * Obtain a new {@link BakedModelRenderer} instance used to render
     * baked models.
     *
     *
     */
    BakedModelRenderer bakedModelRenderer();
}