package net.modificationstation.stationapi.api.client.render;

import net.modificationstation.stationapi.api.client.render.model.BakedModelRenderer;

/**
 * Interface for rendering plug-ins that provide enhanced capabilities
 * for model lighting, buffering and rendering. Such plug-ins implement the
 * enhanced model rendering interfaces specified by the Fabric API.
 */
public interface Renderer {
    /**
     * Obtain a new {@link BakedModelRenderer} instance used to render
     * baked models.
     *
     *
     */
    BakedModelRenderer bakedModelRenderer();
}