package net.modificationstation.stationapi.api.client.render;

import net.modificationstation.stationapi.api.client.render.material.MaterialFinder;
import net.modificationstation.stationapi.api.client.render.material.RenderMaterial;
import net.modificationstation.stationapi.api.client.render.mesh.MutableMesh;
import net.modificationstation.stationapi.api.client.render.model.BakedModelRenderer;
import net.modificationstation.stationapi.api.client.render.model.SpriteFinder;
import net.modificationstation.stationapi.api.client.texture.SpriteAtlasTexture;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.impl.client.render.RendererManager;
import org.jetbrains.annotations.Nullable;

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
     * Obtain a new {@link MutableMesh} instance to build optimized meshes and create baked models
     * with enhanced features.
     *
     * <p>Renderer does not retain a reference to returned instances, so they should be re-used
     * when possible to avoid memory allocation overhead.
     */
    MutableMesh mutableMesh();

    /**
     * Obtain a new {@link MaterialFinder} instance to retrieve standard {@link RenderMaterial}
     * instances.
     *
     * <p>Renderer does not retain a reference to returned instances, so they should be re-used for
     * multiple materials when possible to avoid memory allocation overhead.
     */
    MaterialFinder materialFinder();

    /**
     * Return a material previously registered via {@link #registerMaterial(Identifier, RenderMaterial)}.
     * Will return null if no material was found matching the given identifier.
     */
    @Nullable
    RenderMaterial materialById(Identifier id);

    /**
     * Register a material for re-use by other mods or models within a mod.
     * The registry does not persist registrations - mods must create and register
     * all materials at game initialization.
     *
     * <p>Returns false if a material with the given identifier is already present,
     * leaving the existing material intact.
     */
    boolean registerMaterial(Identifier id, RenderMaterial material);

    SpriteFinder getSpriteFinder(SpriteAtlasTexture atlas);

    /**
     * Obtain a new {@link BakedModelRenderer} instance used to render
     * baked models.
     *
     *
     */
    BakedModelRenderer bakedModelRenderer();

    StateManager stateManager();
}